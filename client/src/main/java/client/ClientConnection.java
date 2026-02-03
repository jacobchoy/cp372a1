package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;
import shared.Protocol;

/**
 * Manages the TCP connection to the server.
 * 
 * This class handles:
 * - Establishing and maintaining the socket connection
 * - Sending commands to the server
 * - Receiving responses from the server
 * - Parsing server messages
 * - Notifying the GUI of state changes
 * RFC Section 5.2: Client MUST wait to send a new command until it has received
 * the response to the previous command.
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String hostname;
    private int port;
    private boolean connected;
    private ServerMessageListener messageListener;

    /**
     * Constructs a new ClientConnection.
     * 
     * @param hostname The server hostname or IP address
     * @param port     The server port number
     */
    public ClientConnection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.connected = false;
    }

    /**
     * Establishes a connection to the server.
     * 
     * @return true if connection was successful, false otherwise
     */
    public boolean connect() {
        try {
            Socket ClientSocket = new Socket(hostname, port);
            this.socket = ClientSocket;
            this.in = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
            this.out = new PrintWriter(ClientSocket.getOutputStream(), true);
            this.connected = true;
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
            return false;
        }
    }

    /**
     * Closes the connection to the server.
     */
    public void disconnect() {
        try {
            socket.close();
            this.connected = false;
        } catch (IOException e) {
            System.err.println("Error disconnecting from the server: " + e.getMessage());
        }
    }

    /**
     * Sends a command to the server.
     * 
     * @param command The command string to send
     * @return true if the command was sent successfully, false otherwise
     */
    public boolean sendCommand(String command) {
        try {
            out.println(command);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending command to the server: " + e.getMessage());
            return false;
        }
    }

    /**
     * Receives a response from the server.
     * 
     * Blocks until a response is received or the connection is closed.
     * 
     * @return The response string from the server, or null if connection closed
     */
    public String receiveResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving response from the server: " + e.getMessage());
            return null;
        }
    }

    /**
     * Checks if the client is currently connected to the server.
     * 
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sets the listener to be notified of server messages (on the EDT).
     *
     * @param listener The listener, or null to clear
     */
    public void setServerMessageListener(ServerMessageListener listener) {
        this.messageListener = listener;
    }

    /**
     * Starts a background thread to listen for server messages.
     * 
     * This thread continuously reads messages from the server and
     * notifies the GUI of updates (e.g., new notes, deleted notes).
     */
    public void startListening() {
        Thread listenerThread = new Thread(() -> {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    handleServerMessage(message);
                }
            } catch (IOException e) {
                System.err.println("Error reading from the server: " + e.getMessage());
            }
        });
        listenerThread.start();
    }

    /**
     * Handles an incoming message from the server.
     * 
     * Parses the message and updates the GUI accordingly.
     * 
     * @param message The message received from the server
     */
    private void handleServerMessage(String message) {
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        if (message.startsWith(Protocol.RESP_ERROR)) {
            String errorMsg = message.substring(Protocol.RESP_ERROR.length()).trim();
            System.err.println("Server Error: " + errorMsg);
            if (messageListener != null) {
                SwingUtilities.invokeLater(() -> messageListener.onError(errorMsg));
            }
        } else if (message.startsWith(Protocol.RESP_OK)) {
            String remainder = message.substring(Protocol.RESP_OK.length()).trim();
            if (messageListener != null) {
                SwingUtilities.invokeLater(() -> messageListener.onOkResponse(remainder));
            }
        } else {
            System.out.println("Received unknown message: " + message);
        }
    }
}
