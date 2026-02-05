package shared;

// protocol constants for client-server communication
public class Protocol {
    public static final String CMD_POST = "POST";
    public static final String CMD_GET = "GET";
    public static final String CMD_PIN = "PIN";
    public static final String CMD_UNPIN = "UNPIN";
    public static final String CMD_SHAKE = "SHAKE";
    public static final String CMD_CLEAR = "CLEAR";
    public static final String CMD_DISCONNECT = "DISCONNECT";

    public static final String GET_PINS = "PINS";

    public static final String RESP_OK = "OK";
    public static final String RESP_ERROR = "ERROR";
    public static final String RESP_BOARD = "BOARD";
    public static final String RESP_NOTE = "NOTE";
    public static final String RESP_COLOURS = "colourS";

    public static final String ERR_INVALID_FORMAT = "INVALID_FORMAT";
    public static final String ERR_OUT_OF_BOUNDS = "OUT_OF_BOUNDS";
    public static final String ERR_COLOUR_NOT_SUPPORTED = "colour_NOT_SUPPORTED";
    public static final String ERR_COMPLETE_OVERLAP = "COMPLETE_OVERLAP";
    public static final String ERR_PIN_NOT_FOUND = "PIN_NOT_FOUND";
    public static final String ERR_NO_NOTE_AT_COORDINATE = "NO_NOTE_AT_COORDINATE";
    public static final String ERR_UNKNOWN_COMMAND = "UNKNOWN_COMMAND";
    public static final String ERR_INTERNAL_ERROR = "INTERNAL_ERROR";

    public static final String DELIMITER = " ";
    public static final String LIST_SEPARATOR = ";";
    public static final String LINE_END = "\n";

    public static final String FILTER_COLOUR = "color=";
    public static final String FILTER_CONTAINS = "contains=";
    public static final String FILTER_REFERS_TO = "refersTo=";

    private Protocol() {
    }
}
