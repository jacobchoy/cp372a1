package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main server class for the Bulletin Board System.
 * 
 * This class initializes and starts the TCP server that listens for client connections.
 * Upon accepting a connection, it spawns a new ClientHandler thread to manage that client.
 * 
 * The server maintains a single BulletinBoard instance that is shared among all client handlers.
 * 
 * RFC Section 2.2: Server startup format:
 * java BBoard <port> <board_width> <board_height> <note_width> <note_height> <colour1> ... <colourN>
 * 
 * @author Team Members
 * @version 1.0
 */
public class ServerMain {
    private static BulletinBoard bulletinBoard;
    private static java.util.List<String> validColours;
    
    /**
     * Main entry point for the server application.
     * 
     * RFC Section 2.2: Command-line arguments:
     * - args[0]: port number
     * - args[1]: board_width
     * - args[2]: board_height
     * - args[3]: note_width
     * - args[4]: note_height
     * - args[5...]: valid colours (one or more)
     * 
     * @param args Command-line arguments as specified in RFC Section 2.2
     */
    public static void main(String[] args) {
        // Implementation will go here
    }
    
    /**
     * Gets the shared BulletinBoard instance.
     * 
     * @return The BulletinBoard instance used by the server
     */
    public static BulletinBoard getBulletinBoard() {
        return bulletinBoard;
    }
    
    /**
     * Gets the list of valid colours for notes.
     * 
     * @return The list of valid colour names
     */
    public static java.util.List<String> getValidColours() {
        return validColours;
    }
}
