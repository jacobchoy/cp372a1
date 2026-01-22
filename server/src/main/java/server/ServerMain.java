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
 * @author Team Members
 * @version 1.0
 */
public class ServerMain {
    private static final int DEFAULT_PORT = 8080;
    private static BulletinBoard bulletinBoard;
    
    /**
     * Main entry point for the server application.
     * 
     * Accepts an optional port number as a command-line argument.
     * If no port is provided, uses the default port (8080).
     * 
     * @param args Command-line arguments. args[0] may contain the port number.
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
}
