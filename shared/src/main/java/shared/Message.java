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
     * Constructs a QUERY command message.
     * 
     * Format: QUERY [noteId] or QUERY (for all notes)
     * 
     * @param noteId The note ID to query, or null to query all notes
     * @return The formatted command string
     */
    public static String buildQueryCommand(String noteId) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a PIN command message.
     * 
     * Format: PIN noteId x y
     * 
     * @param noteId The ID of the note to pin
     * @param x The x-coordinate of the pin
     * @param y The y-coordinate of the pin
     * @return The formatted command string
     */
    public static String buildPinCommand(String noteId, int x, int y) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs an UNPIN command message.
     * 
     * Format: UNPIN pinId
     * 
     * @param pinId The ID of the pin to remove
     * @return The formatted command string
     */
    public static String buildUnpinCommand(String pinId) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Constructs a DELETE command message.
     * 
     * Format: DELETE noteId
     * 
     * @param noteId The ID of the note to delete
     * @return The formatted command string
     */
    public static String buildDeleteCommand(String noteId) {
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
     * Format: ERROR reason
     * 
     * @param reason The error reason
     * @return The formatted response string
     */
    public static String buildErrorResponse(String reason) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Parses a NOTE response message.
     * 
     * Format: NOTE noteId x y color message [pinned]
     * 
     * @param message The message string to parse
     * @return An array containing [noteId, x, y, color, message, pinned] or null if invalid
     */
    public static String[] parseNoteResponse(String message) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Parses a PIN response message.
     * 
     * Format: PIN pinId x y noteId
     * 
     * @param message The message string to parse
     * @return An array containing [pinId, x, y, noteId] or null if invalid
     */
    public static String[] parsePinResponse(String message) {
        // Implementation will go here
        return null;
    }
}
