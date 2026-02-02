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
 * java BBoard &lt;port&gt; &lt;board_width&gt; &lt;board_height&gt; &lt;note_width&gt; &lt;note_height&gt; &lt;colour1&gt; ... &lt;colourN&gt;
 *
 * RFC Section 10.1: One thread per client connection; all threads share the global board.
 *
 * @author Jacob Choy
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
     * The board size is defined here by parsing command-line arguments and
     * passing them to the BulletinBoard constructor.
     * 
     * @param args Command-line arguments as specified in RFC Section 2.2
     */
    public static void main(String[] args) {
        // TODO: Parse command-line arguments
        // - Parse port from args[0]
        // - Parse boardWidth from args[1]
        // - Parse boardHeight from args[2]
        // - Parse noteWidth from args[3]
        // - Parse noteHeight from args[4]
        // - Parse colours from args[5...]
        
        // TODO: Create BulletinBoard with parsed dimensions
        // bulletinBoard = new BulletinBoard(boardWidth, boardHeight, noteWidth, noteHeight);
        
        // TODO: Store valid colours list
        // validColours = ... (from args[5...])
        
        // TODO: Start server socket and accept connections
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
