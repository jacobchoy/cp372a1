package client;

/**
 * Main client class for the Bulletin Board System.
 * 
 * This class initializes and starts the GUI client application.
 * It creates the main window and establishes a connection to the server.
 * 
 * @author Team Members
 * @version 1.0
 */
public class ClientMain {
    
    /**
     * Main entry point for the client application.
     * 
     * Accepts optional command-line arguments:
     * - args[0]: Server hostname or IP address (default: localhost)
     * - args[1]: Server port number (default: 6767)
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        // Implementation will go here
    }
    
    /**
     * Parses command-line arguments and returns the server hostname.
     * 
     * @param args Command-line arguments
     * @return The server hostname, or "localhost" if not specified
     */
    private static String parseHostname(String[] args) {
        // Implementation will go here
        return "localhost";
    }
    
    /**
     * Parses command-line arguments and returns the server port.
     * 
     * @param args Command-line arguments
     * @return The server port number, or 6767 if not specified
     */
    private static int parsePort(String[] args) {
        // Implementation will go here
        return 6767;
    }
}
