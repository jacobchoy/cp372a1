package server;

/**
 * Parses and validates protocol messages from clients.
 * 
 * This class is responsible for:
 * - Parsing command strings into structured data
 * - Validating command syntax and parameters
 * - Extracting command types and arguments
 * - Generating error messages for invalid commands (RFC Section 9.1 error codes).
 *
 * @author Jacob Choy
 * @version 1.0
 */
public class ProtocolParser {

    /**
     * Parses a command string and extracts the command type.
     * RFC Section 6.1: Commands are case-sensitive and MUST be uppercase.
     *
     * @param command The raw command string from the client
     * @return The command type (POST, GET, PIN, UNPIN, SHAKE, CLEAR, DISCONNECT) or null if invalid
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
     * RFC Section 7.1: POST x y colour message... (message is rest of line).
     *
     * @param params The parameter string from the POST command
     * @return An array containing [x, y, colour, message] or null if invalid
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
     * RFC Section 7.2.2: criterion = "color="&lt;colour&gt; | "contains="&lt;INT&gt; SP &lt;INT&gt; | "refersTo="&lt;STRING&gt;
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
     * Generates an ERROR response for an invalid command.
     * RFC Section 8.2: ERROR &lt;ERROR_CODE&gt; &lt;human-readable message&gt;
     *
     * @param errorCode The error code (RFC Section 9.1)
     * @param message The human-readable message
     * @return A formatted ERROR response line
     */
    public String generateErrorMessage(String errorCode, String message) {
        // Implementation will go here
        return "";
    }
}
