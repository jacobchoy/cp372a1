package shared;

// build and parse protocol messages
public class Message {

    // build POST x y colour message line
    public static String buildPostCommand(int x, int y, String colour, String message) {
        return Protocol.CMD_POST + Protocol.DELIMITER + x + Protocol.DELIMITER + y
                + Protocol.DELIMITER + colour + Protocol.DELIMITER
                + (message != null ? message : "") + Protocol.LINE_END;
    }

    // build GET PINS line
    public static String buildGetPinsCommand() {
        return Protocol.CMD_GET + Protocol.DELIMITER + Protocol.GET_PINS + Protocol.LINE_END;
    }

    // build GET line with optional filters (color, contains, refersTo)
    public static String buildGetCommand(String colour, Integer containsX, Integer containsY, String refersTo) {
        StringBuilder sb = new StringBuilder(Protocol.CMD_GET);
        if (colour != null && !colour.isEmpty()) {
            sb.append(Protocol.DELIMITER).append(Protocol.FILTER_COLOUR).append(colour);
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

    // build PIN x y line
    public static String buildPinCommand(int x, int y) {
        return Protocol.CMD_PIN + Protocol.DELIMITER + x + Protocol.DELIMITER + y + Protocol.LINE_END;
    }

    // build UNPIN x y line
    public static String buildUnpinCommand(int x, int y) {
        return Protocol.CMD_UNPIN + Protocol.DELIMITER + x + Protocol.DELIMITER + y + Protocol.LINE_END;
    }

    // build SHAKE line
    public static String buildShakeCommand() {
        return Protocol.CMD_SHAKE + Protocol.LINE_END;
    }

    // build DISCONNECT line
    public static String buildDisconnectCommand() {
        return Protocol.CMD_DISCONNECT + Protocol.LINE_END;
    }

    // build CLEAR line
    public static String buildClearCommand() {
        return Protocol.CMD_CLEAR + Protocol.LINE_END;
    }

    // build OK or OK <data> line
    public static String buildOkResponse(String info) {
        if (info == null || info.isEmpty()) {
            return Protocol.RESP_OK + Protocol.LINE_END;
        }
        return Protocol.RESP_OK + Protocol.DELIMITER + info + Protocol.LINE_END;
    }

    // build ERROR <code> <message> line
    public static String buildErrorResponse(String errorCode, String message) {
        return Protocol.RESP_ERROR + Protocol.DELIMITER + errorCode + Protocol.DELIMITER
                + (message != null ? message : "") + Protocol.LINE_END;
    }

    // build OK BOARD w h NOTE nw nh colourS c1 c2 ... line
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

    // parse "x y colour content;..." into list of [x, y, colour, content]
    public static java.util.List<String[]> parseNoteList(String response) {
        java.util.List<String[]> list = new java.util.ArrayList<>();
        if (response == null || response.trim().isEmpty()) {
            return list;
        }
        String[] segments = response.split(Protocol.LIST_SEPARATOR);
        for (String segment : segments) {
            segment = segment.trim();
            if (segment.isEmpty()) continue;
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

    // parse "x y;x y;..." into list of [x, y]
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

    // format one note as "x y colour content"
    public static String formatNote(int x, int y, String colour, String content) {
        return x + Protocol.DELIMITER + y + Protocol.DELIMITER + colour + Protocol.DELIMITER
                + (content != null ? content : "");
    }

    // format one pin as "x y"
    public static String formatPin(int x, int y) {
        return x + Protocol.DELIMITER + y;
    }
}
