package shared;

/**
 * Shared protocol constants for the Bulletin Board System.
 * 
 * This class defines all protocol command strings and response codes
 * used for communication between the client and server.
 * 
 * Both client and server MUST use these constants to ensure
 * protocol consistency (RFC Section 1.1).
 * 
 * @author Jacob Choy, Jonathan Bilewicz
 * @version 1.0
 */
public class Protocol {
    // Command types (RFC Section 7)
    public static final String CMD_POST = "POST";
    public static final String CMD_GET = "GET";
    public static final String CMD_PIN = "PIN";
    public static final String CMD_UNPIN = "UNPIN";
    public static final String CMD_SHAKE = "SHAKE";
    public static final String CMD_CLEAR = "CLEAR";
    public static final String CMD_DISCONNECT = "DISCONNECT";
    
    // GET subcommands
    public static final String GET_PINS = "PINS";
    
    // Response codes (RFC Section 8)
    public static final String RESP_OK = "OK";
    public static final String RESP_ERROR = "ERROR";
    
    // Response prefixes (RFC Section 8.1)
    public static final String RESP_BOARD = "BOARD";
    public static final String RESP_NOTE = "NOTE";
    public static final String RESP_COLOURS = "colourS";
    
    // Error codes (RFC Section 9.1)
    public static final String ERR_INVALID_FORMAT = "INVALID_FORMAT";
    public static final String ERR_OUT_OF_BOUNDS = "OUT_OF_BOUNDS";
    public static final String ERR_COLOUR_NOT_SUPPORTED = "colour_NOT_SUPPORTED";
    public static final String ERR_COMPLETE_OVERLAP = "COMPLETE_OVERLAP";
    public static final String ERR_PIN_NOT_FOUND = "PIN_NOT_FOUND";
    public static final String ERR_NO_NOTE_AT_COORDINATE = "NO_NOTE_AT_COORDINATE";
    public static final String ERR_UNKNOWN_COMMAND = "UNKNOWN_COMMAND";
    public static final String ERR_INTERNAL_ERROR = "INTERNAL_ERROR";
    
    // Delimiters (RFC Section 6.1)
    public static final String DELIMITER = " ";
    public static final String LIST_SEPARATOR = ";";
    public static final String LINE_END = "\n";
    
    // GET filter prefixes (RFC Section 7.2.2 - criterion syntax uses "color=")
    public static final String FILTER_COLOUR = "color=";
    public static final String FILTER_CONTAINS = "contains=";
    public static final String FILTER_REFERS_TO = "refersTo=";
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Protocol() {
        // Utility class - no instantiation
    }
}
