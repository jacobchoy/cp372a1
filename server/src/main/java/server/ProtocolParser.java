package server;

/**
 * Parses and validates protocol messages from clients.
 * 
 * This class is responsible for:
 * - Parsing command strings into structured data
 * - Validating command syntax and parameters
 * - Extracting command types and arguments
 * - Generating error messages for invalid commands
 * 
 * @author Team Members
 * @version 1.0
 */
public class ProtocolParser {
    
    /**
     * Parses a command string and extracts the command type.
     * 
     * @param command The raw command string from the client
     * @return The command type (e.g., "POST", "QUERY", "PIN", etc.) or null if invalid
     */
    public String parseCommandType(String command) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Extracts the parameters from a command string.
     * 
     * @param command The raw command string from the client
     * @return The parameter portion of the command, or empty string if none
     */
    public String parseParameters(String command) {
        // Implementation will go here
        return "";
    }
    
    /**
     * Parses a POST command and extracts note information.
     * 
     * Expected format: POST x y color message
     * 
     * @param params The parameter string from the POST command
     * @return An array containing [x, y, color, message] or null if invalid
     */
    public String[] parsePostCommand(String params) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Parses a GET command and determines if it's GET PINS or GET with filters.
     * 
     * RFC Section 7.2: GET PINS or GET [filters]
     * 
     * @param params The parameter string from the GET command
     * @return "PINS" if GET PINS, or the filter string otherwise
     */
    public String parseGetCommand(String params) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Parses GET filter criteria.
     * 
     * RFC Section 7.2.2: Filters are colour=<colour>, contains=<x> <y>, refersTo=<substring>
     * 
     * @param params The filter parameter string
     * @return A map or object containing parsed filter criteria, or null if invalid
     */
    public java.util.Map<String, String> parseGetFilters(String params) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Parses a PIN command and extracts coordinates.
     * 
     * RFC Section 7.3: Expected format: PIN x y
     * 
     * @param params The parameter string from the PIN command
     * @return An array containing [x, y] as strings, or null if invalid
     */
    public String[] parsePinCommand(String params) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Parses an UNPIN command and extracts coordinates.
     * 
     * RFC Section 7.4: Expected format: UNPIN x y
     * 
     * @param params The parameter string from the UNPIN command
     * @return An array containing [x, y] as strings, or null if invalid
     */
    public String[] parseUnpinCommand(String params) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Validates that a command string is well-formed.
     * 
     * @param command The command string to validate
     * @return true if the command is valid, false otherwise
     */
    public boolean isValidCommand(String command) {
        // Implementation will go here
        return false;
    }
    
    /**
     * Generates an error message for an invalid command.
     * 
     * @param command The invalid command string
     * @param reason The reason why the command is invalid
     * @return A formatted error message
     */
    public String generateErrorMessage(String command, String reason) {
        // Implementation will go here
        return "";
    }
}
