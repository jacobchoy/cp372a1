package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import server.utils.Logger;
import shared.Protocol;
import utils.ProtocolParser;

// handles communication with a single client connection
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BulletinBoard bulletinBoard;
    private BufferedReader in;
    private PrintWriter out;

    public int idGen;
    private List<String> validColours;

    // constructs a new ClientHandler for the given client socket
    public ClientHandler(Socket clientSocket, BulletinBoard bulletinBoard, List<String> validColours) {
        this.clientSocket = clientSocket;
        this.bulletinBoard = bulletinBoard;
        this.validColours = validColours;

    }

    // main run method executed by the thread
    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // Auto-flush enabled for PrintWriter
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            sendInitialMessage();

            String line;
            while ((line = in.readLine()) != null) {
                if (line.trim().isEmpty())
                    continue;
                Logger.info("Command: " + line);
                if (processCommand(line)) {
                    break;
                }
            }
        } catch (IOException e) {
            Logger.error("Client: " + e.getMessage());
        } finally {
            closeConnection();
        }
    }

    // sends the initial handshake message to the client
    private void sendInitialMessage() {
        out.println(Protocol.RESP_OK + " " + Protocol.RESP_BOARD + " " + bulletinBoard.getBoardWidth() + " "
                + bulletinBoard.getBoardHeight()
                + " " + Protocol.RESP_NOTE + " " + bulletinBoard.getNoteWidth() + " " + bulletinBoard.getNoteHeight()
                + " " + Protocol.RESP_COLOURS + " " + String.join(" ", validColours));
        this.idGen = 0;
    }

    // processes a single command from the client
    private boolean processCommand(String command) {
        if (!ProtocolParser.isValidCommand(command)) {
            out.println(Protocol.RESP_ERROR + " " + Protocol.ERR_UNKNOWN_COMMAND + " Unknown command");
            return false;
        }

        String commandType = ProtocolParser.parseCommandType(command);
        String params = ProtocolParser.parseParameters(command);
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

        return "DISCONNECT".equals(commandType);
    }

    // handles the POST command to add a new note
    private String handlePostNote(String params) {
        String[] parts = ProtocolParser.parsePostCommand(params);
        if (parts == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid POST format";
        }

        try {
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            String colour = parts[2];
            String message = parts[3];

            // RFC: note content MUST NOT contain semicolon (list separator in responses)
            if (message != null && message.contains(";")) {
                return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Note content must not contain semicolon";
            }

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

    // handles the GET command to retrieve notes or pins
    private String handleGet(String params) {
        String parsed = ProtocolParser.parseGetCommand(params);
        if (parsed == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid GET format";
        }

        if (parsed.equals(Protocol.GET_PINS)) {
            return handleGetPins();
        } else {
            return handleGetWithFilters(parsed);
        }
    }

    // handles GET PINS subcommand
    private String handleGetPins() {
        List<Pin> pins = bulletinBoard.getPins();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pins.size(); i++) {
            Pin p = pins.get(i);
            sb.append(p.getX()).append(" ").append(p.getY());
            if (i < pins.size() - 1) {
                sb.append(Protocol.LIST_SEPARATOR);
            }
        }
        return sb.length() > 0 ? Protocol.RESP_OK + " " + sb.toString() : Protocol.RESP_OK;
    }

    // handles GET with filter criteria
    private String handleGetWithFilters(String params) {
        Map<String, String> filters = ProtocolParser.parseGetFilters(params);
        if (filters == null) {
            return Protocol.RESP_ERROR + " " + Protocol.ERR_INVALID_FORMAT + " Invalid filter format";
        }

        List<Note> allNotes = bulletinBoard.getNotes();
        List<Note> result = new ArrayList<>();

        for (Note note : allNotes) {
            boolean matches = true;

            // Filter: colour
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

        // Format response, OK x y colour message or OK if no matches
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

    // handles the PIN command to add a pin at coordinates
    private String handlePin(String params) {
        String[] parts = ProtocolParser.parsePinCommand(params);
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

    // handles the UNPIN command to remove a pin at coordinates
    private String handleUnpin(String params) {
        String[] parts = ProtocolParser.parseUnpinCommand(params);
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

    // handles the SHAKE command to remove all unpinned notes
    private String handleShake() {
        bulletinBoard.shake();
        return Protocol.RESP_OK;
    }

    // handles the CLEAR command to remove all notes and pins
    private String handleClear() {
        bulletinBoard.clear();
        return Protocol.RESP_OK;
    }

    // handles the DISCONNECT command to close the connection
    private String handleDisconnect() {
        return Protocol.RESP_OK;
    }

    // closes the client connection and cleans up resources
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
