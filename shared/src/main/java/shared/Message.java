package shared;

/**
 * Represents a message in the protocol.
 * 
 * This class provides utilities for constructing and parsing
 * protocol messages between client and server.
 * 
 * Messages follow a specific format defined in the RFC.
 * 
 * @author Team Members
 * @version 1.0
 */
public class Message {
    
    /**
     * Constructs a POST command message.
     * 
     * Format: POST x y color message
     * 
     * @param x The x-coordinate of the note
     * @param y The y-coordinate of the note
     * @param color The color of the note
     * @param message The message text
     * @return The formatted command string
     */
    public static String buildPostCommand(int x, int y, String color, String message) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a GET PINS command message.
     * 
     * RFC Section 7.2.1: Format: GET PINS
     * 
     * @return The formatted command string
     */
    public static String buildGetPinsCommand() {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a GET command with filter criteria.
     * 
     * RFC Section 7.2.2: Format: GET [colour=<colour>] [contains=<x> <y>] [refersTo=<substring>]
     * 
     * @param colour Optional colour filter (null if not filtering by colour)
     * @param containsX Optional x-coordinate for contains filter (null if not filtering)
     * @param containsY Optional y-coordinate for contains filter (null if not filtering)
     * @param refersTo Optional substring for refersTo filter (null if not filtering)
     * @return The formatted command string
     */
    public static String buildGetCommand(String colour, Integer containsX, Integer containsY, String refersTo) {
        // Implementation will go here
        return "";
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
        // Implementation will go here
        return "";
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
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a SHAKE command message.
     * 
     * RFC Section 7.5: Format: SHAKE
     * 
     * @return The formatted command string
     */
    public static String buildShakeCommand() {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a DISCONNECT command message.
     * 
     * RFC Section 7.7: Format: DISCONNECT
     * 
     * @return The formatted command string
     */
    public static String buildDisconnectCommand() {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a CLEAR command message.
     * 
     * Format: CLEAR
     * 
     * @return The formatted command string
     */
    public static String buildClearCommand() {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs an OK response message.
     * 
     * Format: OK [additional info]
     * 
     * @param info Additional information to include
     * @return The formatted response string
     */
    public static String buildOkResponse(String info) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs an ERROR response message.
     * 
     * RFC Section 8.2: Format: ERROR <ERROR_CODE> <human-readable message>
     * 
     * @param errorCode The error code (e.g., "OUT_OF_BOUNDS", "INVALID_FORMAT")
     * @param message The human-readable error message
     * @return The formatted response string
     */
    public static String buildErrorResponse(String errorCode, String message) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs the handshake response message.
     * 
     * RFC Section 8.1: Format: OK BOARD <board_width> <board_height> NOTE <note_width> <note_height> colourS <colour1> <colour2> ... <colourN>
     * 
     * @param boardWidth The board width
     * @param boardHeight The board height
     * @param noteWidth The note width
     * @param noteHeight The note height
     * @param colours The list of valid colours
     * @return The formatted handshake response string
     */
    public static String buildHandshakeResponse(int boardWidth, int boardHeight, int noteWidth, int noteHeight, java.util.List<String> colours) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Parses a note list from GET response.
     * 
     * RFC Section 7.2.2: Format: OK x y colour content;x y colour content;...
     * 
     * @param response The response string (without "OK " prefix)
     * @return A list of note data arrays, each containing [x, y, colour, content]
     */
    public static java.util.List<String[]> parseNoteList(String response) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Parses a pin list from GET PINS response.
     * 
     * RFC Section 7.2.1: Format: OK x1 y1;x2 y2;...
     * 
     * @param response The response string (without "OK " prefix)
     * @return A list of pin coordinate arrays, each containing [x, y]
     */
    public static java.util.List<String[]> parsePinList(String response) {
        // Implementation will go here
        return null;
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
        // Implementation will go here
        return "";
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
        // Implementation will go here
        return "";
    }
}
