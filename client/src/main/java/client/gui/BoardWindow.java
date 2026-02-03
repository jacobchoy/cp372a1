package client.gui;

import client.ClientConnection;
import client.ServerMessageListener;
import shared.Colours;
import shared.Protocol;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Main GUI window for the bulletin board client.
 * 
 * This window displays:
 * - The bulletin board canvas showing all notes and pins
 * - Controls for posting new notes
 * - Controls for querying, pinning, and deleting notes
 * - Status messages and error notifications
 * 
 * The window maintains a connection to the server and updates the display when the board state changes.
 * RFC Section 1.2: GUI layout and UI features are not covered by the RFC; this is client implementation.
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class BoardWindow extends JFrame implements ServerMessageListener {
    private final ClientConnection connection;
    private final int boardWidth;
    private final int boardHeight;
    private final int noteWidth;
    private final int noteHeight;

    private JPanel boardPanel;
    private final List<NoteWidget> noteWidgets = new ArrayList<>();
    private final List<PinWidget> pinWidgets = new ArrayList<>();
    private JLabel statusLabel;
    private String lastSentCommand = "";

    private JTextField postXField;
    private JTextField postYField;
    private JComboBox<String> colourCombo;
    private JTextField messageField;
    private JTextField getColourField;
    private JTextField getContainsXField;
    private JTextField getContainsYField;
    private JTextField getRefersToField;
    private JTextField pinXField;
    private JTextField pinYField;
    private JTextField unpinXField;
    private JTextField unpinYField;

    public BoardWindow(ClientConnection connection, int boardWidth, int boardHeight,
                       int noteWidth, int noteHeight) {
        this.connection = connection;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.noteWidth = noteWidth;
        this.noteHeight = noteHeight;

        setTitle("Bulletin Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeGUI();
        connection.setServerMessageListener(this);
    }

    private void initializeGUI() {
        setLayout(new BorderLayout(5, 5));

        JPanel controls = createControlPanel();
        add(controls, BorderLayout.NORTH);

        boardPanel = createBoardPanel();
        JScrollPane scrollPane = new JScrollPane(boardPanel);
        add(scrollPane, BorderLayout.CENTER);

        statusLabel = new JLabel("Ready.");
        add(statusLabel, BorderLayout.SOUTH);

        pack();
        setSize(Math.max(500, boardWidth + 50), Math.max(400, boardHeight + 150));
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        panel.add(new JLabel("POST:"));
        postXField = new JTextField(3);
        postYField = new JTextField(3);
        postXField.setToolTipText("x");
        postYField.setToolTipText("y");
        panel.add(postXField);
        panel.add(postYField);
        colourCombo = new JComboBox<>(Colours.getAvailableColours().toArray(new String[0]));
        panel.add(colourCombo);
        messageField = new JTextField(15);
        messageField.setToolTipText("Message");
        panel.add(messageField);
        JButton postBtn = new JButton("Post");
        postBtn.addActionListener(e -> handlePostNote());
        panel.add(postBtn);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        panel.add(new JLabel("GET:"));
        getColourField = new JTextField(6);
        getColourField.setToolTipText("color=...");
        getContainsXField = new JTextField(2);
        getContainsYField = new JTextField(2);
        getRefersToField = new JTextField(8);
        panel.add(getColourField);
        panel.add(getContainsXField);
        panel.add(getContainsYField);
        panel.add(getRefersToField);
        JButton getBtn = new JButton("Get");
        getBtn.addActionListener(e -> handleGet());
        panel.add(getBtn);
        JButton getPinsBtn = new JButton("Get Pins");
        getPinsBtn.addActionListener(e -> handleGetPins());
        panel.add(getPinsBtn);

        panel.add(new JSeparator(SwingConstants.VERTICAL));

        panel.add(new JLabel("PIN:"));
        pinXField = new JTextField(3);
        pinYField = new JTextField(3);
        panel.add(pinXField);
        panel.add(pinYField);
        JButton pinBtn = new JButton("Pin");
        pinBtn.addActionListener(e -> handlePin());
        panel.add(pinBtn);

        panel.add(new JLabel("UNPIN:"));
        unpinXField = new JTextField(3);
        unpinYField = new JTextField(3);
        panel.add(unpinXField);
        panel.add(unpinYField);
        JButton unpinBtn = new JButton("Unpin");
        unpinBtn.addActionListener(e -> handleUnpin());
        panel.add(unpinBtn);

        JButton shakeBtn = new JButton("Shake");
        shakeBtn.addActionListener(e -> handleShake());
        panel.add(shakeBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> handleClear());
        panel.add(clearBtn);

        JButton disconnectBtn = new JButton("Disconnect");
        disconnectBtn.addActionListener(e -> handleDisconnect());
        panel.add(disconnectBtn);

        return panel;
    }

    private JPanel createBoardPanel() {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintBoard(g);
            }
        };
        panel.setPreferredSize(new Dimension(boardWidth, boardHeight));
        panel.setBackground(new Color(240, 240, 220));
        panel.setOpaque(true);
        return panel;
    }

    private void handlePostNote() {
        String xStr = postXField.getText().trim();
        String yStr = postYField.getText().trim();
        String colour = (String) colourCombo.getSelectedItem();
        String message = messageField.getText();
        if (xStr.isEmpty() || yStr.isEmpty()) {
            showError("Enter x and y for POST");
            return;
        }
        try {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            String cmd = Protocol.CMD_POST + " " + x + " " + y + " " + colour + " " + (message != null ? message : "");
            lastSentCommand = Protocol.CMD_POST;
            if (connection.sendCommand(cmd)) {
                showStatus("Posting note...");
            }
        } catch (NumberFormatException e) {
            showError("x and y must be numbers");
        }
    }

    private void handleGet() {
        StringBuilder cmd = new StringBuilder(Protocol.CMD_GET);
        String colorVal = getColourField.getText().trim();
        if (!colorVal.isEmpty()) {
            cmd.append(" ").append(Protocol.FILTER_COLOUR).append(colorVal);
        }
        String cx = getContainsXField.getText().trim();
        String cy = getContainsYField.getText().trim();
        if (!cx.isEmpty() && !cy.isEmpty()) {
            cmd.append(" ").append(Protocol.FILTER_CONTAINS).append(cx).append(" ").append(cy);
        }
        String refers = getRefersToField.getText().trim();
        if (!refers.isEmpty()) {
            cmd.append(" ").append(Protocol.FILTER_REFERS_TO).append(refers);
        }
        lastSentCommand = "GET";
        if (connection.sendCommand(cmd.toString().trim())) {
            showStatus("Getting notes...");
        }
    }

    private void handleGetPins() {
        lastSentCommand = "GET_PINS";
        if (connection.sendCommand(Protocol.CMD_GET + " " + Protocol.GET_PINS)) {
            showStatus("Getting pins...");
        }
    }

    private void handlePin() {
        String xStr = pinXField.getText().trim();
        String yStr = pinYField.getText().trim();
        if (xStr.isEmpty() || yStr.isEmpty()) {
            showError("Enter x and y for PIN");
            return;
        }
        try {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            lastSentCommand = Protocol.CMD_PIN;
            if (connection.sendCommand(Protocol.CMD_PIN + " " + x + " " + y)) {
                showStatus("Adding pin...");
            }
        } catch (NumberFormatException e) {
            showError("x and y must be numbers");
        }
    }

    private void handleUnpin() {
        String xStr = unpinXField.getText().trim();
        String yStr = unpinYField.getText().trim();
        if (xStr.isEmpty() || yStr.isEmpty()) {
            showError("Enter x and y for UNPIN");
            return;
        }
        try {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            lastSentCommand = Protocol.CMD_UNPIN;
            if (connection.sendCommand(Protocol.CMD_UNPIN + " " + x + " " + y)) {
                showStatus("Removing pin...");
            }
        } catch (NumberFormatException e) {
            showError("x and y must be numbers");
        }
    }

    private void handleShake() {
        lastSentCommand = Protocol.CMD_SHAKE;
        if (connection.sendCommand(Protocol.CMD_SHAKE)) {
            showStatus("Shaking...");
        }
    }

    private void handleClear() {
        lastSentCommand = Protocol.CMD_CLEAR;
        if (connection.sendCommand(Protocol.CMD_CLEAR)) {
            showStatus("Clearing board...");
        }
    }

    private void handleDisconnect() {
        connection.sendCommand(Protocol.CMD_DISCONNECT);
        connection.disconnect();
        showStatus("Disconnected.");
        connection.setServerMessageListener(null);
    }

    @Override
    public void onError(String message) {
        showError(message);
        lastSentCommand = "";
    }

    @Override
    public void onOkResponse(String remainder) {
        if ("GET".equals(lastSentCommand)) {
            applyNotesFromGetResponse(remainder);
            lastSentCommand = "GET_PINS";
            connection.sendCommand(Protocol.CMD_GET + " " + Protocol.GET_PINS);
        } else if ("GET_PINS".equals(lastSentCommand)) {
            applyPinsFromGetPinsResponse(remainder);
            showStatus("Board updated.");
            lastSentCommand = "";
        } else if (Protocol.CMD_POST.equals(lastSentCommand) || Protocol.CMD_PIN.equals(lastSentCommand)
                || Protocol.CMD_UNPIN.equals(lastSentCommand) || Protocol.CMD_SHAKE.equals(lastSentCommand)
                || Protocol.CMD_CLEAR.equals(lastSentCommand)) {
            showStatus("Done.");
            refreshBoard();
        } else {
            showStatus("OK");
            lastSentCommand = "";
        }
    }

    private void refreshBoard() {
        lastSentCommand = "GET";
        connection.sendCommand(Protocol.CMD_GET);
    }

    private void applyNotesFromGetResponse(String remainder) {
        if (remainder == null || remainder.trim().isEmpty()) {
            noteWidgets.clear();
            repaintBoard();
            return;
        }
        String[] segments = remainder.split("\\" + Protocol.LIST_SEPARATOR);
        noteWidgets.clear();
        for (String seg : segments) {
            seg = seg.trim();
            if (seg.isEmpty()) continue;
            String[] parts = seg.split("\\s+", 4);
            if (parts.length >= 3) {
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    String colour = parts[2];
                    String message = parts.length > 3 ? parts[3] : "";
                    String noteId = x + "_" + y;
                    boolean pinned = isNotePinned(x, y);
                    NoteWidget w = new NoteWidget(noteId, x, y, colour, message, pinned, noteWidth, noteHeight);
                    noteWidgets.add(w);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        repaintBoard();
    }

    private void applyPinsFromGetPinsResponse(String remainder) {
        if (remainder == null || remainder.trim().isEmpty()) {
            pinWidgets.clear();
            updateNotePinnedState();
            repaintBoard();
            return;
        }
        String[] segments = remainder.split("\\" + Protocol.LIST_SEPARATOR);
        pinWidgets.clear();
        int idx = 0;
        for (String seg : segments) {
            seg = seg.trim();
            if (seg.isEmpty()) continue;
            String[] parts = seg.split("\\s+");
            if (parts.length >= 2) {
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    String pinId = "pin_" + (idx++);
                    PinWidget pw = new PinWidget(pinId, x, y, null);
                    pinWidgets.add(pw);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        updateNotePinnedState();
        repaintBoard();
    }

    private boolean isNotePinned(int noteX, int noteY) {
        for (PinWidget p : pinWidgets) {
            int px = p.getX();
            int py = p.getY();
            if (px >= noteX && px < noteX + noteWidth && py >= noteY && py < noteY + noteHeight) {
                return true;
            }
        }
        return false;
    }

    private void updateNotePinnedState() {
        for (NoteWidget n : noteWidgets) {
            n.setPinned(isNotePinned(n.getX(), n.getY()));
        }
    }

    private void repaintBoard() {
        boardPanel.removeAll();
        for (NoteWidget w : noteWidgets) {
            w.setBounds(w.getX(), w.getY(), noteWidth, noteHeight);
            boardPanel.add(w);
        }
        for (PinWidget w : pinWidgets) {
            w.setBounds(w.getX(), w.getY(), 10, 10);
            boardPanel.add(w);
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void updateBoard() {
        refreshBoard();
    }

    public void addNoteWidget(NoteWidget noteWidget) {
        noteWidgets.add(noteWidget);
        boardPanel.add(noteWidget);
        noteWidget.setBounds(noteWidget.getX(), noteWidget.getY(), noteWidth, noteHeight);
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void removeNoteWidget(String noteId) {
        noteWidgets.removeIf(n -> noteId.equals(n.getNoteId()));
        repaintBoard();
    }

    public void showError(String message) {
        statusLabel.setText("Error: " + message);
        statusLabel.setForeground(Color.RED);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.BLACK);
    }

    private void paintBoard(Graphics g) {
        g.setColor(new Color(240, 240, 220));
        g.fillRect(0, 0, boardWidth, boardHeight);
        g.setColor(Color.GRAY);
        g.drawRect(0, 0, boardWidth - 1, boardHeight - 1);
    }
}
