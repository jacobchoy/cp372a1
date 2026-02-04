package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.SwingUtilities;
import shared.Protocol;

// manages the TCP connection to the server
public class ClientConnection {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String hostname;
    private int port;
    private boolean connected;
    private ServerMessageListener messageListener;

    // constructs a new ClientConnection
    public ClientConnection(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        this.connected = false;
    }

    // establishes a connection to the server
    public boolean connect() {
        try {
            Socket clientSocket = new Socket(hostname, port);
            this.socket = clientSocket;
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.connected = true;
            return true;
        } catch (IOException e) {
            System.err.println("Error connecting to the server: " + e.getMessage());
            return false;
        }
    }

    // closes the connection to the server
    public void disconnect() {
        try {
            socket.close();
            this.connected = false;
        } catch (IOException e) {
            System.err.println("Error disconnecting from the server: " + e.getMessage());
        }
    }

    // sends a command to the server
    public boolean sendCommand(String command) {
        try {
            out.println(command);
            return true;
        } catch (Exception e) {
            System.err.println("Error sending command to the server: " + e.getMessage());
            return false;
        }
    }

    // receives a response from the server
    public String receiveResponse() {
        try {
            return in.readLine();
        } catch (IOException e) {
            System.err.println("Error receiving response from the server: " + e.getMessage());
            return null;
        }
    }

    // checks if the client is currently connected to the server
    public boolean isConnected() {
        return connected;
    }

    // sets the listener to be notified of server messages
    public void setServerMessageListener(ServerMessageListener listener) {
        this.messageListener = listener;
    }

    // starts a background thread to listen for server messages
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

    // handles an incoming message from the server
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
