package shared;

/**
 * Represents a message in the protocol.
 * 
 * This class provides utilities for constructing and parsing
 * protocol messages between client and server.
 * 
 * Messages follow the format in RFC Section 6. Each command/response is a single
 * line of UTF-8 text terminated by newline (RFC Section 5.1).
 *
 * @author Jacob Choy, Jonathan Bilewicz
 * @version 1.0
 */
public class Message {

    /**
     * Constructs a POST command message.
     *
     * RFC Section 7.1: POST x y colour message... (message is rest of line; content MAY contain spaces)
     * 
     * @param x The x-coordinate of the note
     * @param y The y-coordinate of the note
     * @param color The color of the note
     * @param message The message text
     * @return The formatted command string
     */
    public static String buildPostCommand(int x, int y, String color, String message) {
        // RFC 7.1: POST x y colour message... (fields separated by space; message is rest of line)
        return Protocol.CMD_POST + Protocol.DELIMITER + x + Protocol.DELIMITER + y
                + Protocol.DELIMITER + color + Protocol.DELIMITER
                + (message != null ? message : "") + Protocol.LINE_END;
    }

    /**
     * Constructs a GET PINS command message.
     * 
     * RFC Section 7.2.1: Format: GET PINS
     * 
     * @return The formatted command string
     */
    public static String buildGetPinsCommand() {
        return Protocol.CMD_GET + Protocol.DELIMITER + Protocol.GET_PINS + Protocol.LINE_END;
    }
    
    /**
     * Constructs a GET command with filter criteria.
     *
     * RFC Section 7.2.2: GET [color=&lt;colour&gt;] [contains=&lt;x&gt; &lt;y&gt;] [refersTo=&lt;substring&gt;]
     * Any combination of criteria; missing criteria imply no filtering. Combined with logical AND.
     *
     * @param colour Optional colour filter (null if not filtering by colour)
     * @param containsX Optional x-coordinate for contains filter (null if not filtering)
     * @param containsY Optional y-coordinate for contains filter (null if not filtering)
     * @param refersTo Optional substring for refersTo filter (null if not filtering)
     * @return The formatted command string
     */
    public static String buildGetCommand(String colour, Integer containsX, Integer containsY, String refersTo) {
        StringBuilder sb = new StringBuilder(Protocol.CMD_GET);
        if (colour != null && !colour.isEmpty()) {
            sb.append(Protocol.DELIMITER).append(Protocol.FILTER_COLOR).append(colour);
        }
        if (containsX != null && containsY != null) {
            sb.append(Protocol.DELIMITER).append(Protocol.FILTER_CONTAINS).append(containsX).append(Protocol.DELIMITER).append(containsY);
        }
        if (refersTo != null && !refersTo.isEmpty()) {
            sb.append(Protocol.DELIMITER).append(Protocol.FILTER_REFERS_TO).append(refersTo);
        }
        sb.append(Protocol.LINE_END);
        return sb.toString();
    }
    
    /**
     * Constructs a PIN command message.
     * 
     * RFC Section 7.3: Format: PIN x y
     * 
     * @param x The x-coordinate where to place the pin
     * @param y The y-coordinate where to place the pin
     * @return The formatted command string
     */
    public static String buildPinCommand(int x, int y) {
        return Protocol.CMD_PIN + Protocol.DELIMITER + x + Protocol.DELIMITER + y + Protocol.LINE_END;
    }

    /**
     * Constructs an UNPIN command message.
     * 
     * RFC Section 7.4: Format: UNPIN x y
     * 
     * @param x The x-coordinate of the pin to remove
     * @param y The y-coordinate of the pin to remove
     * @return The formatted command string
     */
    public static String buildUnpinCommand(int x, int y) {
        return Protocol.CMD_UNPIN + Protocol.DELIMITER + x + Protocol.DELIMITER + y + Protocol.LINE_END;
    }

    /**
     * Constructs a SHAKE command message.
     * 
     * RFC Section 7.5: Format: SHAKE
     * 
     * @return The formatted command string
     */
    public static String buildShakeCommand() {
        return Protocol.CMD_SHAKE + Protocol.LINE_END;
    }

    /**
     * Constructs a DISCONNECT command message.
     * 
     * RFC Section 7.7: Format: DISCONNECT
     * 
     * @return The formatted command string
     */
    public static String buildDisconnectCommand() {
        return Protocol.CMD_DISCONNECT + Protocol.LINE_END;
    }

    /**
     * Constructs a CLEAR command message.
     *
     * RFC Section 7.6: CLEAR - removes all notes and all pins (atomic).
     *
     * @return The formatted command string
     */
    public static String buildClearCommand() {
        return Protocol.CMD_CLEAR + Protocol.LINE_END;
    }
    
    /**
     * Constructs a success response message.
     *
     * RFC Section 8.1: OK or OK SP &lt;data&gt;
     *
     * @param info Additional data (e.g. note list, pin list) or empty for plain OK
     * @return The formatted response string
     */
    public static String buildOkResponse(String info) {
        if (info == null || info.isEmpty()) {
            return Protocol.RESP_OK + Protocol.LINE_END;
        }
        return Protocol.RESP_OK + Protocol.DELIMITER + info + Protocol.LINE_END;
    }

    /**
     * Constructs an ERROR response message.
     * 
     * RFC Section 8.2: Format: ERROR <ERROR_CODE> <human-readable message>
     * 
     * @param errorCode The error code (e.g., "OUT_OF_BOUNDS", "INVALID_FORMAT")
     * @param message The human-readable error message (RFC Section 8.2)
     * @return The formatted response string
     */
    public static String buildErrorResponse(String errorCode, String message) {
        return Protocol.RESP_ERROR + Protocol.DELIMITER + errorCode + Protocol.DELIMITER
                + (message != null ? message : "") + Protocol.LINE_END;
    }
    
    /**
     * Constructs the handshake response message.
     *
     * RFC Section 8.1: OK BOARD &lt;board_width&gt; &lt;board_height&gt; NOTE &lt;note_width&gt; &lt;note_height&gt; colourS &lt;colour1&gt; &lt;colour2&gt; ... &lt;colourN&gt;
     * Sent immediately upon accepting a new client connection (RFC Section 2.2).
     * 
     * @param boardWidth The board width
     * @param boardHeight The board height
     * @param noteWidth The note width
     * @param noteHeight The note height
     * @param colours The list of valid colours
     * @return The formatted handshake response string
     */
    public static String buildHandshakeResponse(int boardWidth, int boardHeight, int noteWidth, int noteHeight, java.util.List<String> colours) {
        StringBuilder sb = new StringBuilder();
        sb.append(Protocol.RESP_OK).append(Protocol.DELIMITER).append(Protocol.RESP_BOARD)
                .append(Protocol.DELIMITER).append(boardWidth).append(Protocol.DELIMITER).append(boardHeight)
                .append(Protocol.DELIMITER).append(Protocol.RESP_NOTE)
                .append(Protocol.DELIMITER).append(noteWidth).append(Protocol.DELIMITER).append(noteHeight)
                .append(Protocol.DELIMITER).append(Protocol.RESP_COLOURS);
        if (colours != null) {
            for (String c : colours) {
                sb.append(Protocol.DELIMITER).append(c);
            }
        }
        sb.append(Protocol.LINE_END);
        return sb.toString();
    }
    
    /**
     * Parses a note list from GET response.
     *
     * RFC Section 6.2.3 note_repr: &lt;INT&gt; SP &lt;INT&gt; SP &lt;colour&gt; SP &lt;STRING&gt; separated by ";"
     * 
     * @param response The response string (without "OK " prefix)
     * @return A list of note data arrays, each containing [x, y, colour, content]
     */
    public static java.util.List<String[]> parseNoteList(String response) {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        if (response == null || response.trim().isEmpty()) {
            return list;
        }
        String[] segments = response.split(Protocol.LIST_SEPARATOR);
        for (String segment : segments) {
            segment = segment.trim();
            if (segment.isEmpty()) continue;
            // Format: x y colour content (content may contain spaces)
            int endOfX = segment.indexOf(Protocol.DELIMITER);
            if (endOfX < 0) continue;
            int endOfY = segment.indexOf(Protocol.DELIMITER, endOfX + 1);
            if (endOfY < 0) continue;
            int endOfColour = segment.indexOf(Protocol.DELIMITER, endOfY + 1);
            if (endOfColour < 0) continue;
            String x = segment.substring(0, endOfX).trim();
            String y = segment.substring(endOfX + 1, endOfY).trim();
            String colour = segment.substring(endOfY + 1, endOfColour).trim();
            String content = segment.substring(endOfColour + 1).trim();
            list.add(new String[] { x, y, colour, content });
        }
        return list;
    }
    
    /**
     * Parses a pin list from GET PINS response.
     *
     * RFC Section 7.2.1: If pins exist OK x1 y1;x2 y2;... ; pin_repr is &lt;INT&gt; SP &lt;INT&gt;
     * 
     * @param response The response string (without "OK " prefix)
     * @return A list of pin coordinate arrays, each containing [x, y]
     */
    public static java.util.List<String[]> parsePinList(String response) {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        if (response == null || response.trim().isEmpty()) {
            return list;
        }
        String[] segments = response.split(Protocol.LIST_SEPARATOR);
        for (String segment : segments) {
            segment = segment.trim();
            if (segment.isEmpty()) continue;
            String[] parts = segment.split(Protocol.DELIMITER);
            if (parts.length >= 2) {
                list.add(new String[] { parts[0].trim(), parts[1].trim() });
            }
        }
        return list;
    }
    
    /**
     * Formats a note for response output.
     * 
     * RFC Section 7.2.2: Format: x y colour content
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @param colour The note colour
     * @param content The note content
     * @return The formatted note string
     */
    public static String formatNote(int x, int y, String colour, String content) {
        return x + Protocol.DELIMITER + y + Protocol.DELIMITER + colour + Protocol.DELIMITER
                + (content != null ? content : "");
    }

    /**
     * Formats a pin for response output.
     * 
     * RFC Section 7.2.1: Format: x y
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The formatted pin string
     */
    public static String formatPin(int x, int y) {
        return x + Protocol.DELIMITER + y;
    }
}
