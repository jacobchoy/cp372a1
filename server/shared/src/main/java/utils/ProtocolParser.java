package utils;

import shared.Protocol;

// parses client command strings
public class ProtocolParser {

    // return POST, GET, PIN, etc. or null
    public static String parseCommandType(String command) {
        if (command == null)
            return null;
        if (command.startsWith(Protocol.CMD_POST))
            return Protocol.CMD_POST;
        if (command.startsWith(Protocol.CMD_GET))
            return Protocol.CMD_GET;
        if (command.startsWith(Protocol.CMD_PIN))
            return Protocol.CMD_PIN;
        if (command.startsWith(Protocol.CMD_UNPIN))
            return Protocol.CMD_UNPIN;
        if (command.startsWith(Protocol.CMD_SHAKE))
            return Protocol.CMD_SHAKE;
        if (command.startsWith(Protocol.CMD_CLEAR))
            return Protocol.CMD_CLEAR;
        if (command.startsWith(Protocol.CMD_DISCONNECT))
            return Protocol.CMD_DISCONNECT;
        return null;
    }

    // return everything after the command word
    public static String parseParameters(String command) {
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

    // return [x, y, colour, message] or null
    public static String[] parsePostCommand(String params) {
        if (params == null || params.trim().isEmpty())
            return null;
        String[] parts = params.trim().split(Protocol.DELIMITER, 4);
        if (parts.length < 4)
            return null;
        return new String[] { parts[0], parts[1], parts[2], parts[3] };
    }

    // return PINS or filter string (empty = all notes)
    public static String parseGetCommand(String params) {
        if (params == null)
            return null;
        String trimmed = params.trim();
        if (trimmed.equals(Protocol.GET_PINS))
            return Protocol.GET_PINS;
        return trimmed;
    }

    // return map of filter key -> value (color, contains, refersTo)
    public static java.util.Map<String, String> parseGetFilters(String params) {
        if (params == null)
            return null;
        String trimmed = params.trim();
        java.util.Map<String, String> filters = new java.util.HashMap<>();
        if (trimmed.isEmpty())
            return filters;
        String[] parts = trimmed.split(Protocol.DELIMITER);
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty())
                continue;
            String[] keyValue = part.split("=", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                if ("contains".equals(key) && i + 1 < parts.length) {
                    String next = parts[i + 1].trim();
                    if (!next.isEmpty() && !next.contains("=")) {
                        value = value + " " + next;
                        i++;
                    }
                }
                filters.put(key, value);
            }
        }
        return filters;
    }

    // return [x, y] for PIN or null
    public static String[] parsePinCommand(String params) {
        if (params == null)
            return null;
        String trimmed = params.trim();
        if (trimmed.isEmpty())
            return null;
        String[] commandParts = trimmed.split(Protocol.DELIMITER);
        if (commandParts.length != 2)
            return null;
        return new String[] { commandParts[0], commandParts[1] };
    }

    // return [x, y] for UNPIN or null
    public static String[] parseUnpinCommand(String params) {
        if (params == null)
            return null;
        String trimmed = params.trim();
        if (trimmed.isEmpty())
            return null;
        String[] commandParts = trimmed.split(Protocol.DELIMITER);
        if (commandParts.length != 2)
            return null;
        return new String[] { commandParts[0], commandParts[1] };
    }

    // true if command starts with a known command word
    public static boolean isValidCommand(String command) {
        if (command == null)
            return false;
        String trimmed = command.trim();
        if (trimmed.isEmpty())
            return false;
        String[] validCommands = {
                Protocol.CMD_POST, Protocol.CMD_GET, Protocol.CMD_PIN, Protocol.CMD_UNPIN,
                Protocol.CMD_SHAKE, Protocol.CMD_CLEAR, Protocol.CMD_DISCONNECT
        };
        for (String cmd : validCommands) {
            if (trimmed.startsWith(cmd))
                return true;
        }
        return false;
    }

    // build ERROR response line
    public static String generateErrorMessage(String errorCode, String message) {
        return shared.Message.buildErrorResponse(errorCode, message != null ? message : "");
    }
}
