package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import shared.Protocol;

/**
 * Handles communication with a single client connection.
 * 
 * Each ClientHandler runs in its own thread and manages the full lifecycle
 * of a client connection, including:
 * - Receiving and parsing client requests
 * - Processing protocol commands
 * - Sending responses back to the client
 * - Handling connection errors and cleanup
 * 
 * RFC Section 10.1: One thread per client connection; all threads share the
 * global board.
 * RFC Section 5.2: For each command (except DISCONNECT), server sends exactly
 * one response line.
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BulletinBoard bulletinBoard;
    private BufferedReader in;
    private PrintWriter out;
    private ProtocolParser parser;
    public int idGen;
    private List<String> validColours;

    /**
     * Constructs a new ClientHandler for the given client socket.
     * 
     * @param clientSocket  The socket connected to the client
     * @param bulletinBoard The shared BulletinBoard instance
     * @param validColours  The list of valid colours for notes
     */
    public ClientHandler(Socket clientSocket, BulletinBoard bulletinBoard, List<String> validColours) {
        this.clientSocket = clientSocket;
        this.bulletinBoard = bulletinBoard;
        this.validColours = validColours;
        this.parser = new ProtocolParser();
    }

    /**
     * Main run method executed by the thread.
     * 
     * Handles the client connection lifecycle:
     * 1. Sends initial connection message (board dimensions, note dimensions)
     * 2. Processes client commands until connection is closed
     * 3. Cleans up resources on exit
     */
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Auto-flush enabled for PrintWriter
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            sendInitialMessage();

            String line;
            while ((line = in.readLine()) != null) {
                // If client just sends empty lines/whitespace?
                if (line.trim().isEmpty())
                    continue;
                processCommand(line);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    /**
     * Sends the initial handshake message to the client.
     * 
     * RFC Section 8.1: OK BOARD <board_width> <board_height> NOTE
     * <note_width> <note_height> colourS <colour1> ...
     * <colourN>
     * Sent immediately upon accepting a new client connection (RFC Section 2.2).
     */
    private void sendInitialMessage() {
        out.println(Protocol.RESP_OK + " " + Protocol.RESP_BOARD + " " + bulletinBoard.getBoardWidth() + " "
                + bulletinBoard.getBoardHeight()
                + " " + Protocol.RESP_NOTE + " " + bulletinBoard.getNoteWidth() + " " + bulletinBoard.getNoteHeight()
                + " " + Protocol.RESP_COLOURS + " " + String.join(" ", validColours));
        this.idGen = 0;
    }

    /**
     * Processes a single command from the client.
     * 
     * Parses the command and executes the appropriate action on the bulletin board.
     * Sends a response back to the client.
     * 
     * @param command The command string received from the client
     */
    private void processCommand(String command) {
        if (!parser.isValidCommand(command)) {
            out.println(Protocol.RESP_ERROR + " " + Protocol.ERR_UNKNOWN_COMMAND + " Unknown command");
            return;
        }

        String commandType = parser.parseCommandType(command);
        String params = parser.parseParameters(command);
        String response = "";

        try {
            switch (commandType) {
                case "POST":
                    response = handlePostNote(params);
                    break;
                case "GET":
                    response = handleGet(params);
                    break;
                case "PIN":
                    response = handlePin(params);
                    break;
                case "UNPIN":
                    response = handleUnpin(params);
                    break;
                case "SHAKE":
                    response = handleShake();
                    break;
                case "CLEAR":
                    response = handleClear();
                    break;
                case "DISCONNECT":
                    response = handleDisconnect();
                    break;
                default:
                    response = Protocol.RESP_ERROR + " " + Protocol.ERR_UNKNOWN_COMMAND + " Unknown command type";
            }
        } catch (Exception e) {
            response = Protocol.RESP_ERROR + " " + Protocol.ERR_INTERNAL_ERROR + " " + e.getMessage();
        }

        if (response != null && !response.isEmpty()) {
            out.println(response);
        }
    }

    /**
     * Handles the POST command to add a new note.
     * 
     * @param params The parameters for the POST command
     * @return The response message to send to the client
     */
    private String handlePostNote(String params) {
        String[] parts = parser.parsePostCommand(params);
        if (parts == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid POST format";
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            String colour = parts[2];
            String message = parts[3];

            // Validate colour
            boolean validColour = false;
            for (String c : validColours) {
                if (c.equals(colour)) {
                    validColour = true;
                    break;
                }
            }
            if (!validColour) {
                return Protocol.RESP_ERROR + " " + Protocol.ERR_COLOUR_NOT_SUPPORTED + " Colour not supported";
            }

            // Check bounds manually to distinguish from overlap error
            if (x < 0 || y < 0 || x + bulletinBoard.getNoteWidth() > bulletinBoard.getBoardWidth() ||
                    y + bulletinBoard.getNoteHeight() > bulletinBoard.getBoardHeight()) {
                return Protocol.RESP_ERROR + " " + Protocol.ERR_OUT_OF_BOUNDS + " Note out of bounds";
            }

            Note note = new Note(String.valueOf(idGen), x, y, colour, message);
            boolean success = bulletinBoard.addNote(note);

            if (success) {
                idGen++;
                return Protocol.RESP_OK;
            } else {
                return Protocol.RESP_ERROR + " " + Protocol.ERR_COMPLETE_OVERLAP + " Note overlaps completely";
            }

        } catch (NumberFormatException e) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Coordinates must be integers";
        }
    }

    /**
     * Handles the GET command to retrieve notes or pins.
     * 
     * RFC Section 7.2: GET PINS or GET [color=<colour>] [contains=<x>
     * <y>] [refersTo=<substring>]
     * 
     * @param params The parameters for the GET command
     * @return The response message to send to the client
     */
    private String handleGet(String params) {
        String parsed = parser.parseGetCommand(params);
        if (parsed == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid GET format";
        }

        if (parsed.equals(Protocol.GET_PINS)) {
            return handleGetPins();
        } else {
            return handleGetWithFilters(parsed);
        }
    }

    /**
     * Handles GET PINS subcommand.
     * 
     * RFC Section 7.2.1: Returns all pin coordinates as "x1 y1;x2 y2;..."
     * 
     * @return The response message with pin coordinates
     */
    private String handleGetPins() {
        List<Pin> pins = bulletinBoard.getPins();
        StringBuilder sb = new StringBuilder();
        // RFC says: coordinates. Format usually implies list of Pins
        // Typically GET PINS returns "x y;x y" etc.
        // Assuming format: x1 y1;x2 y2
        for (int i = 0; i < pins.size(); i++) {
            Pin p = pins.get(i);
            sb.append(p.getX()).append(" ").append(p.getY());
            if (i < pins.size() - 1) {
                sb.append(Protocol.LIST_SEPARATOR);
            }
        }
        return sb.length() > 0 ? Protocol.RESP_OK + " " + sb.toString() : Protocol.RESP_OK;
    }

    /**
     * Handles GET with filter criteria.
     * 
     * RFC Section 7.2.2: GET [color=<colour>] [contains=<x> <y>]
     * [refersTo=<substring>]; criteria combined with logical AND.
     * 
     * @param params The filter parameters
     * @return The response message with matching notes as "x y colour content;..."
     */
    private String handleGetWithFilters(String params) {
        Map<String, String> filters = parser.parseGetFilters(params);
        if (filters == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid filter format";
        }

        List<Note> allNotes = bulletinBoard.getNotes();
        List<Note> result = new ArrayList<>();

        for (Note note : allNotes) {
            boolean matches = true;

            // Filter: colour (protocol key is "color=")
            if (filters.containsKey("color")) {
                if (!note.getColour().equals(filters.get("color"))) {
                    matches = false;
                }
            }

            // Filter: refersTo
            if (matches && filters.containsKey("refersTo")) {
                if (!note.getMessage().contains(filters.get("refersTo"))) {
                    matches = false;
                }
            }

            // Filter: contains (x y)
            if (matches && filters.containsKey("contains")) {
                String val = filters.get("contains"); // "x y"
                try {
                    String[] coords = val.trim().split("\\s+");
                    if (coords.length == 2) {
                        int cx = Integer.parseInt(coords[0]);
                        int cy = Integer.parseInt(coords[1]);
                        if (!note.containsPoint(cx, cy, bulletinBoard.getNoteWidth(), bulletinBoard.getNoteHeight())) {
                            matches = false;
                        }
                    } else {
                        // Invalid contains format, ignore or fail?
                        // parser might have validated it or passed raw string.
                        // For now let's assume if it fails parsing we just say it doesn't match
                        matches = false;
                    }
                } catch (NumberFormatException e) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(note);
            }
        }

        // Format response: OK x y colour message;... or OK if no matches (RFC 7.2.2)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.size(); i++) {
            Note n = result.get(i);
            sb.append(n.getX()).append(" ").append(n.getY()).append(" ")
                    .append(n.getColour()).append(" ").append(n.getMessage());
            if (i < result.size() - 1) {
                sb.append(Protocol.LIST_SEPARATOR);
            }
        }
        return sb.length() > 0 ? Protocol.RESP_OK + " " + sb.toString() : Protocol.RESP_OK;
    }

    /**
     * Handles the PIN command to add a pin at coordinates.
     * 
     * RFC Section 7.3: PIN x y places a pin at (x, y).
     * All notes covering that coordinate become pinned.
     * 
     * @param params The parameters for the PIN command (x y)
     * @return The response message to send to the client
     */
    private String handlePin(String params) {
        String[] parts = parser.parsePinCommand(params);
        if (parts == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid PIN format";
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            boolean success = bulletinBoard.addPin(x, y);
            if (success) {
                return Protocol.RESP_OK;
            } else {
                return Protocol.RESP_ERROR + " " + Protocol.ERR_NO_NOTE_AT_COORDINATE + " No note at coordinate";
            }
        } catch (NumberFormatException e) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Coordinates must be integers";
        }
    }

    /**
     * Handles the UNPIN command to remove a pin at coordinates.
     * 
     * RFC Section 7.4: UNPIN x y removes one pin at (x, y).
     * 
     * @param params The parameters for the UNPIN command (x y)
     * @return The response message to send to the client
     */
    private String handleUnpin(String params) {
        String[] parts = parser.parseUnpinCommand(params);
        if (parts == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid UNPIN format";
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);

            boolean success = bulletinBoard.removePin(x, y);
            if (success) {
                return Protocol.RESP_OK;
            } else {
                return Protocol.RESP_ERROR + " " + Protocol.ERR_PIN_NOT_FOUND + " Pin not found";
            }
        } catch (NumberFormatException e) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Coordinates must be integers";
        }
    }

    /**
     * Handles the SHAKE command to remove all unpinned notes.
     * 
     * RFC Section 7.5: SHAKE removes all unpinned notes.
     * The operation MUST be atomic.
     * 
     * @return The response message to send to the client
     */
    private String handleShake() {
        bulletinBoard.shake();
        return Protocol.RESP_OK;
    }

    /**
     * Handles the CLEAR command to remove all notes and pins.
     * 
     * RFC Section 7.6: CLEAR removes all notes and all pins.
     * The operation MUST be atomic.
     * 
     * @return The response message to send to the client
     */
    private String handleClear() {
        bulletinBoard.clear();
        return Protocol.RESP_OK;
    }

    /**
     * Handles the DISCONNECT command to close the connection.
     *
     * RFC Section 7.7: Server may send OK before closing or close immediately; MUST
     * clean up resources; MUST NOT crash on unexpected disconnect.
     * 
     * @return The response message (OK) or null if closing immediately
     */
    private String handleDisconnect() {
        return Protocol.RESP_OK;
    }

    /**
     * Closes the client connection and cleans up resources.
     * RFC Section 11: If client disconnects unexpectedly, server MUST clean up and
     * continue serving other clients.
     */
    private void closeConnection() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
