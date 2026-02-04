package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import server.utils.Logger;

// main server class for the Bulletin Board System
public class ServerMain {
    private static BulletinBoard bulletinBoard;
    private static java.util.List<String> validColours;

    // main entry point for the server application
    public static void main(String[] args) {
        if (args == null || args.length < 6) {
            System.err.println(
                    "Usage: java BBoard <port> <board_width> <board_height> <note_width> <note_height> <colour1> ... <colourN>");
            System.exit(1);
        }
        int port = Integer.parseInt(args[0]);
        int boardWidth = Integer.parseInt(args[1]);
        int boardHeight = Integer.parseInt(args[2]);
        int noteWidth = Integer.parseInt(args[3]);
        int noteHeight = Integer.parseInt(args[4]);
        validColours = new ArrayList<>(Arrays.asList(args).subList(5, args.length));

        bulletinBoard = new BulletinBoard(boardWidth, boardHeight, noteWidth, noteHeight);
        Logger.initialize("bulletin_board_server.log");

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.info("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, bulletinBoard, validColours);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            System.exit(1);
        }
    }

    // gets the shared BulletinBoard instance
    public static BulletinBoard getBulletinBoard() {
        return bulletinBoard;
    }

    // gets the list of valid colours for notes
    public static java.util.List<String> getValidColours() {
        return validColours;
    }
}
