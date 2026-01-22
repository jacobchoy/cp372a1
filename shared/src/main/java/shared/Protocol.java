package shared;

/**
 * Shared protocol constants for the Bulletin Board System.
 * 
 * This class defines all protocol command strings and response codes
 * used for communication between the client and server.
 * 
 * Both client and server should use these constants to ensure
 * protocol consistency.
 * 
 * @author Team Members
 * @version 1.0
 */
public class Protocol {
    // Command types
    public static final String CMD_POST = "POST";
    public static final String CMD_QUERY = "QUERY";
    public static final String CMD_PIN = "PIN";
    public static final String CMD_UNPIN = "UNPIN";
    public static final String CMD_DELETE = "DELETE";
    public static final String CMD_CLEAR = "CLEAR";
    
    // Response codes
    public static final String RESP_OK = "OK";
    public static final String RESP_ERROR = "ERROR";
    
    // Response prefixes
    public static final String RESP_NOTE = "NOTE";
    public static final String RESP_PIN = "PIN";
    public static final String RESP_BOARD = "BOARD";
    
    // Delimiters
    public static final String DELIMITER = " ";
    public static final String LINE_END = "\n";
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Protocol() {
        // Utility class - no instantiation
    }
}
