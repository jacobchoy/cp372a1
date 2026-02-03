package client;

import client.gui.BoardWindow;
import shared.Protocol;

import javax.swing.SwingUtilities;

/**
 * Main client class for the Bulletin Board System.
 * 
 * This class initializes and starts the GUI client application.
 * It creates the main window and establishes a connection to the server.
 * RFC Section 2.2: Client establishes TCP connection to server's IP address and port.
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class ClientMain {

    /** Default server hostname when not specified. */
    private static final String DEFAULT_HOST = "localhost";

    /** Default server port when not specified. */
    private static final int DEFAULT_PORT = 6767;

    /**
     * Main entry point for the client application.
     * Accepts optional command-line arguments:
     * - args[0]: Server hostname or IP address (default: localhost)
     * - args[1]: Server port number (default: 6767)
     * 
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        String hostname = parseHostname(args);
        int port = parsePort(args);

        ClientConnection connection = new ClientConnection(hostname, port);
        if (!connection.connect()) {
            System.err.println("Failed to connect to server at " + hostname + ":" + port);
            return;
        }

        // RFC Section 8.1: Server sends OK BOARD <board_width> <board_height> NOTE <note_width> <note_height> colourS ... on connect
        String initialResponse = connection.receiveResponse();
        if (initialResponse == null) {
            System.err.println("No initial response from server.");
            connection.disconnect();
            return;
        }

        int[] dimensions = parseInitialBoardDimensions(initialResponse);
        if (dimensions == null) {
            System.err.println("Invalid initial board message from server: " + initialResponse);
            connection.disconnect();
            return;
        }

        int boardWidth = dimensions[0];
        int boardHeight = dimensions[1];
        int noteWidth = dimensions[2];
        int noteHeight = dimensions[3];

        connection.startListening();

        final ClientConnection conn = connection;
        final int bw = boardWidth;
        final int bh = boardHeight;
        final int nw = noteWidth;
        final int nh = noteHeight;

        SwingUtilities.invokeLater(() -> {
            BoardWindow window = new BoardWindow(conn, bw, bh, nw, nh, null);
            window.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
        });
    }

    /**
     * Parses the initial OK BOARD ... response to extract board and note dimensions.
     * Format: OK BOARD &lt;board_width&gt; &lt;board_height&gt; NOTE &lt;note_width&gt; &lt;note_height&gt; colourS ...
     *
     * @param message The initial response from the server
     * @return int[4] { boardWidth, boardHeight, noteWidth, noteHeight } or null if parse fails
     */
    private static int[] parseInitialBoardDimensions(String message) {
        if (message == null || !message.startsWith(Protocol.RESP_OK + " " + Protocol.RESP_BOARD)) {
            return null;
        }
        String rest = message.substring((Protocol.RESP_OK + " " + Protocol.RESP_BOARD).length()).trim();
        String[] parts = rest.split("\\s+");
        // Expect: <board_width> <board_height> NOTE <note_width> <note_height> colourS ...
        if (parts.length < 6) {
            return null;
        }
        if (!Protocol.RESP_NOTE.equals(parts[2])) {
            return null;
        }
        try {
            int boardWidth = Integer.parseInt(parts[0]);
            int boardHeight = Integer.parseInt(parts[1]);
            int noteWidth = Integer.parseInt(parts[3]);
            int noteHeight = Integer.parseInt(parts[4]);
            return new int[] { boardWidth, boardHeight, noteWidth, noteHeight };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parses command-line arguments and returns the server hostname.
     *
     * @param args Command-line arguments
     * @return The server hostname, or "localhost" if not specified
     */
    private static String parseHostname(String[] args) {
        if (args != null && args.length > 0 && args[0] != null && !args[0].trim().isEmpty()) {
            return args[0].trim();
        }
        return DEFAULT_HOST;
    }

    /**
     * Parses command-line arguments and returns the server port.
     *
     * @param args Command-line arguments
     * @return The server port number, or 6767 if not specified or invalid
     */
    private static int parsePort(String[] args) {
        if (args != null && args.length > 1 && args[1] != null && !args[1].trim().isEmpty()) {
            try {
                int port = Integer.parseInt(args[1].trim());
                if (port > 0 && port <= 65535) {
                    return port;
                }
            } catch (NumberFormatException e) {
                // fall through to default
            }
        }
        return DEFAULT_PORT;
    }
}
