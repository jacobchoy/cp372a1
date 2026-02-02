package server;

import shared.Protocol;

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
        if (command == null) return null;
        if (command.startsWith(Protocol.CMD_POST)) return Protocol.CMD_POST;
        if (command.startsWith(Protocol.CMD_GET)) return Protocol.CMD_GET;
        if (command.startsWith(Protocol.CMD_PIN)) return Protocol.CMD_PIN;
        if (command.startsWith(Protocol.CMD_UNPIN)) return Protocol.CMD_UNPIN;
        if (command.startsWith(Protocol.CMD_SHAKE)) return Protocol.CMD_SHAKE;
        if (command.startsWith(Protocol.CMD_CLEAR)) return Protocol.CMD_CLEAR;
        if (command.startsWith(Protocol.CMD_DISCONNECT)) return Protocol.CMD_DISCONNECT;
        return null;
    }
    
    /**
     * Extracts the parameters from a command string.
     * 
     * @param command The raw command string from the client
     * @return The parameter portion of the command, or empty string if none
     */
    public String parseParameters(String command) {
        if (command.startsWith(Protocol.CMD_POST)) {
            return command.substring(Protocol.CMD_POST.length()).trim();
        } else if (command.startsWith(Protocol.CMD_GET)) {
            return command.substring(Protocol.CMD_GET.length()).trim();
        } else if (command.startsWith(Protocol.CMD_PIN)) {
            return command.substring(Protocol.CMD_PIN.length()).trim();
        } else if (command.startsWith(Protocol.CMD_UNPIN)) {
            return command.substring(Protocol.CMD_UNPIN.length()).trim();
        } else if (command.startsWith(Protocol.CMD_SHAKE)) {
            return command.substring(Protocol.CMD_SHAKE.length()).trim();
        } else if (command.startsWith(Protocol.CMD_CLEAR)) {
            return command.substring(Protocol.CMD_CLEAR.length()).trim();
        } else if (command.startsWith(Protocol.CMD_DISCONNECT)) {
            return command.substring(Protocol.CMD_DISCONNECT.length()).trim();
        }
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
        if (params == null || params.trim().isEmpty()) return null;
        String[] parts = params.trim().split(Protocol.DELIMITER, 4);
        if (parts.length < 4) return null;
        return new String[] { parts[0], parts[1], parts[2], parts[3] };
    }
    
    /**
     * Parses a GET command and determines if it's GET PINS or GET with filters.
     *
     * RFC Section 7.2: GET PINS or GET [filters]
     * params is everything after "GET " (e.g. "PINS" or "color=red refersTo=meeting").
     *
     * @param params The parameter string from the GET command (after "GET ")
     * @return Protocol.GET_PINS ("PINS") if GET PINS, or the full filter string for GET with filters; null if empty/invalid
     */
    public String parseGetCommand(String params) {
        if (params == null) return null;
        String trimmed = params.trim();
        if (trimmed.isEmpty()) return null;
        if (trimmed.equals(Protocol.GET_PINS)) return Protocol.GET_PINS;
        return trimmed;
    }
    
    /**
     * Parses GET filter criteria.
     * RFC Section 7.2.2: criterion = "color="&lt;colour&gt; | "contains="&lt;INT&gt; SP &lt;INT&gt; | "refersTo="&lt;STRING&gt;
     *
     * @param params The filter parameter string
     * @return A map or object containing parsed filter criteria, or null if invalid
     */
    public java.util.Map<String, String> parseGetFilters(String params) {
        if (params == null) return null;
        String trimmed = params.trim();
        if (trimmed.isEmpty()) return null;
        java.util.Map<String, String> filters = new java.util.HashMap<>();
        String[] parts = trimmed.split(Protocol.DELIMITER);
        for (String part : parts) {
            part = part.trim();
            if (part.isEmpty()) continue;
            String[] keyValue = part.split("=", 2);
            if (keyValue.length == 2) {
                filters.put(keyValue[0], keyValue[1]);
            }
        }
        return filters; 
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
        if (params == null) return null;
        String trimmed = params.trim();
        if (trimmed.isEmpty()) return null; 
        String[] commandParts = trimmed.split(Protocol.DELIMITER);
        if (commandParts.length != 2) return null;
        return new String[] { commandParts[0], commandParts[1] };
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
        if (params == null) return null;
        String trimmed = params.trim();
        if (trimmed.isEmpty()) return null;
        String[] commandParts = trimmed.split(Protocol.DELIMITER);
        if (commandParts.length != 2) return null;
        return new String[] { commandParts[0], commandParts[1] };
    }
    
    /**
     * Validates that a command string is well-formed.
     *
     * @param command The command string to validate
     * @return true if the command is valid, false otherwise
     */
    public boolean isValidCommand(String command) {
        if (command == null) return false;
        String trimmed = command.trim();
        if (trimmed.isEmpty()) return false;
        String[] validCommands = {
            Protocol.CMD_POST, Protocol.CMD_GET, Protocol.CMD_PIN, Protocol.CMD_UNPIN,
            Protocol.CMD_SHAKE, Protocol.CMD_CLEAR, Protocol.CMD_DISCONNECT
        };
        for (String cmd : validCommands) {
            if (trimmed.startsWith(cmd)) return true;
        }
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
        return shared.Message.buildErrorResponse(errorCode, message != null ? message : "");
    }
}
