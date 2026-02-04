package client.gui;

import client.ClientConnection;
import client.ServerMessageListener;
import shared.Colours;
import shared.Protocol;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Main GUI window for the bulletin board client.
 * 
 * This window displays:
 * - The bulletin board canvas showing all notes and pins
 * - Controls for posting new notes
 * - Controls for querying, pinning, and deleting notes
 * - Status messages and error notifications
 * 
 * The window maintains a connection to the server and updates the display when
 * the board state changes.
 * RFC Section 1.2: GUI layout and UI features are not covered by the RFC; this
 * is client implementation.
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
    private final List<String> availableColours;

    private JPanel boardPanel;
    private final List<NoteWidget> noteWidgets = new ArrayList<>();
    private final List<PinWidget> pinWidgets = new ArrayList<>();
    private JLabel statusLabel;
    private String lastSentCommand = "";
    private boolean getWasUserInitiated;
    private boolean getHadFilters;
    private String lastGetFilterDescription;
    private JTextArea getLogArea;

    private JTextField postXField;
    private JTextField postYField;
    private JComboBox<String> colourCombo;
    private JTextField messageField;
    private JComboBox<String> getColourCombo;
    private JTextField getContainsXField;
    private JTextField getContainsYField;
    private JTextField getRefersToField;
    private JTextField pinXField;
    private JTextField pinYField;
    private JTextField unpinXField;
    private JTextField unpinYField;

    public BoardWindow(ClientConnection connection, int boardWidth, int boardHeight,
            int noteWidth, int noteHeight, List<String> availableColours) {
        this.connection = connection;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.noteWidth = noteWidth;
        this.noteHeight = noteHeight;
        this.availableColours = availableColours;

        setTitle("Bulletin Board");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeGUI();
        connection.setServerMessageListener(this);
        // Load current board state as soon as window is ready
        refreshBoard();
    }

    private static final Color WINDOW_BG = new Color(245, 245, 247);
    private static final Color BOARD_BG = Color.WHITE;
    private static final Color BOARD_BORDER = new Color(220, 220, 224);

    private void initializeGUI() {
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(WINDOW_BG);

        JPanel controls = createControlPanel();
        controls.setBackground(WINDOW_BG);
        add(controls, BorderLayout.NORTH);

        boardPanel = createBoardPanel();
        JScrollPane scrollPane = new JScrollPane(boardPanel);
        scrollPane.setBackground(WINDOW_BG);
        scrollPane.getViewport().setBackground(WINDOW_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(BOARD_BORDER, 1));
        add(scrollPane, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new BorderLayout(0, 8));
        eastPanel.setBackground(WINDOW_BG);
        eastPanel.add(createBoardInfoPanel(), BorderLayout.NORTH);
        eastPanel.add(createGetLogPanel(), BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        statusLabel = new JLabel("Ready.");
        statusLabel.setBackground(WINDOW_BG);
        add(statusLabel, BorderLayout.SOUTH);

        // static size so it looks good :)
        setSize(700, 750);
        setResizable(false);
    }

    private JPanel createBoardInfoPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBackground(WINDOW_BG);

        int endX = Math.max(0, boardWidth - 1);
        int endY = Math.max(0, boardHeight - 1);
        JLabel info = new JLabel("<html>Board: " + boardWidth + "×" + boardHeight + "<br>Note: " + noteWidth + "×"
                + noteHeight + "<br>Coords: (0,0)–(" + endX + "," + endY + ")</html>");
        info.setBackground(WINDOW_BG);
        panel.add(info);
        return panel;
    }

    private JPanel createGetLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(BOARD_BORDER, 1),
                "Activity log", 0, 0, null, Color.DARK_GRAY));
        panel.setBackground(WINDOW_BG);
        panel.setPreferredSize(new Dimension(220, 180));

        getLogArea = new JTextArea(10, 20);
        getLogArea.setEditable(false);
        getLogArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        getLogArea.setLineWrap(true);
        getLogArea.setWrapStyleWord(true);
        getLogArea.setMargin(new Insets(4, 4, 4, 4));
        JScrollPane scroll = new JScrollPane(getLogArea);
        scroll.setPreferredSize(new Dimension(220, 180));
        panel.add(scroll, BorderLayout.CENTER);

        JButton clearLogBtn = new JButton("Clear log");
        clearLogBtn.addActionListener(e -> getLogArea.setText(""));
        panel.add(clearLogBtn, BorderLayout.SOUTH);
        return panel;
    }

    private static final String[] COLOUR_OPTIONS = new String[] { "red", "blue", "green" };

    private JPanel createControlPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(WINDOW_BG);
        Border border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BOARD_BORDER, 1),
                BorderFactory.createEmptyBorder(6, 6, 6, 6));

        // POST (RFC 7.1): add note at (x,y) with colour and message
        JPanel postBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        postBox.setBackground(WINDOW_BG);
        postBox.setBorder(BorderFactory.createTitledBorder(border, "POST", 0, 0, null, Color.DARK_GRAY));
        postBox.add(new JLabel("x:"));
        postXField = new JTextField(3);
        postBox.add(postXField);
        postBox.add(new JLabel("y:"));
        postYField = new JTextField(3);
        postBox.add(postYField);
        postBox.add(new JLabel("colour:"));
        colourCombo = new JComboBox<>(COLOUR_OPTIONS);
        postBox.add(colourCombo);
        postBox.add(new JLabel("message:"));
        messageField = new JTextField(10);
        postBox.add(messageField);
        JButton postBtn = new JButton("Post");
        postBtn.addActionListener(e -> handlePostNote());
        postBox.add(postBtn);
        panel.add(postBox);

        // GET (RFC 7.2): retrieve notes. Optional filters: colour, contains (x,y),
        // message contains text.
        JPanel getBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        getBox.setBackground(WINDOW_BG);
        getBox.setBorder(
                BorderFactory.createTitledBorder(border, "GET notes (optional filters)", 0, 0, null, Color.DARK_GRAY));
        getBox.add(new JLabel("colour:"));
        getColourCombo = new JComboBox<>(new String[] { "(all)", "red", "blue", "green" });
        getBox.add(getColourCombo);
        getBox.add(new JLabel("contains x:"));
        getContainsXField = new JTextField(2);
        getBox.add(getContainsXField);
        getBox.add(new JLabel("y:"));
        getContainsYField = new JTextField(2);
        getBox.add(getContainsYField);
        getBox.add(new JLabel("message contains:"));
        getRefersToField = new JTextField(8);
        getRefersToField.setToolTipText("Show only notes whose message contains this text");
        getBox.add(getRefersToField);
        JButton getBtn = new JButton("Get");
        getBtn.addActionListener(e -> handleGet());
        getBox.add(getBtn);
        panel.add(getBox);

        // GET PINS (RFC 7.2.1): retrieve pin coordinates only
        JPanel getPinsBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        getPinsBox.setBackground(WINDOW_BG);
        getPinsBox.setBorder(BorderFactory.createTitledBorder(border, "GET PINS", 0, 0, null, Color.DARK_GRAY));
        JButton getPinsBtn = new JButton("Get Pins");
        getPinsBtn.addActionListener(e -> handleGetPins());
        getPinsBox.add(getPinsBtn);
        panel.add(getPinsBox);

        // PIN (RFC 7.3): place pin at (x,y); note covering that point is pinned
        JPanel pinBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        pinBox.setBackground(WINDOW_BG);
        pinBox.setBorder(BorderFactory.createTitledBorder(border, "PIN", 0, 0, null, Color.DARK_GRAY));
        pinBox.add(new JLabel("x:"));
        pinXField = new JTextField(3);
        pinBox.add(pinXField);
        pinBox.add(new JLabel("y:"));
        pinYField = new JTextField(3);
        pinBox.add(pinYField);
        JButton pinBtn = new JButton("Pin");
        pinBtn.addActionListener(e -> handlePin());
        pinBox.add(pinBtn);
        panel.add(pinBox);

        // UNPIN (RFC 7.4): remove pin at (x,y)
        JPanel unpinBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        unpinBox.setBackground(WINDOW_BG);
        unpinBox.setBorder(BorderFactory.createTitledBorder(border, "UNPIN", 0, 0, null, Color.DARK_GRAY));
        unpinBox.add(new JLabel("x:"));
        unpinXField = new JTextField(3);
        unpinBox.add(unpinXField);
        unpinBox.add(new JLabel("y:"));
        unpinYField = new JTextField(3);
        unpinBox.add(unpinYField);
        JButton unpinBtn = new JButton("Unpin");
        unpinBtn.addActionListener(e -> handleUnpin());
        unpinBox.add(unpinBtn);
        panel.add(unpinBox);

        // SHAKE (RFC 7.5) / CLEAR (RFC 7.6) / DISCONNECT (RFC 7.7)
        JPanel actionsBox = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 2));
        actionsBox.setBackground(WINDOW_BG);
        actionsBox.setBorder(BorderFactory.createTitledBorder(border, "Actions", 0, 0, null, Color.DARK_GRAY));
        JButton shakeBtn = new JButton("Shake");
        shakeBtn.addActionListener(e -> handleShake());
        actionsBox.add(shakeBtn);
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> handleClear());
        actionsBox.add(clearBtn);
        JButton disconnectBtn = new JButton("Disconnect");
        disconnectBtn.addActionListener(e -> handleDisconnect());
        actionsBox.add(disconnectBtn);
        panel.add(actionsBox);

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
        panel.setOpaque(false);
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
        if (message == null || message.trim().isEmpty()) {
            showError("Enter a message for POST");
            return;
        }
        if (colour == null || colour.trim().isEmpty()) {
            showError("Select a colour for POST");
            return;
        }
        try {
            int x = Integer.parseInt(xStr);
            int y = Integer.parseInt(yStr);
            String cmd = Protocol.CMD_POST + " " + x + " " + y + " " + colour.trim() + " " + message.trim();
            lastSentCommand = Protocol.CMD_POST;
            activityLog("POST", x + " " + y + " " + colour.trim() + " \"" + message.trim() + "\"");
            if (connection.sendCommand(cmd)) {
                showStatus("Posting note...");
            }
        } catch (NumberFormatException e) {
            showError("x and y must be numbers");
        }
    }

    private void handleGet() {
        getWasUserInitiated = true;
        StringBuilder cmd = new StringBuilder(Protocol.CMD_GET);
        StringBuilder filterDesc = new StringBuilder();
        String colourVal = null;
        try {
            Object sel = getColourCombo.getSelectedItem();
            if (sel != null && !"(all)".equals(sel.toString().trim())) {
                colourVal = sel.toString().trim();
                cmd.append(" ").append(Protocol.FILTER_COLOUR).append(colourVal);
                if (filterDesc.length() > 0)
                    filterDesc.append(" ");
                filterDesc.append("colour=").append(colourVal);
            }
        } catch (Exception ignored) {
        }
        String cx = getContainsXField.getText().trim();
        String cy = getContainsYField.getText().trim();
        if (!cx.isEmpty() && !cy.isEmpty()) {
            cmd.append(" ").append(Protocol.FILTER_CONTAINS).append(cx).append(" ").append(cy);
            if (filterDesc.length() > 0)
                filterDesc.append(" ");
            filterDesc.append("contains=").append(cx).append(",").append(cy);
        }
        String refers = getRefersToField.getText().trim();
        if (!refers.isEmpty()) {
            cmd.append(" ").append(Protocol.FILTER_REFERS_TO).append(refers);
            if (filterDesc.length() > 0)
                filterDesc.append(" ");
            filterDesc.append("refersTo=\"").append(refers).append("\"");
        }
        String getCmd = cmd.toString().trim();
        if (filterDesc.length() > 0) {
            lastSentCommand = "GET_FILTERED";
            lastGetFilterDescription = filterDesc.toString();
            getHadFilters = true;
        } else {
            lastSentCommand = "GET";
            lastGetFilterDescription = null;
            getHadFilters = false;
        }
        activityLog("GET", filterDesc.length() > 0 ? "filter: " + filterDesc : "full board");
        if (connection.sendCommand(getCmd)) {
            showStatus("Refreshing board...");
        }
    }

    private void handleGetPins() {
        lastSentCommand = "GET_PINS";
        activityLog("GET_PINS", "");
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
            activityLog("PIN", x + " " + y);
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
            activityLog("UNPIN", x + " " + y);
            if (connection.sendCommand(Protocol.CMD_UNPIN + " " + x + " " + y)) {
                showStatus("Removing pin...");
            }
        } catch (NumberFormatException e) {
            showError("x and y must be numbers");
        }
    }

    private void handleShake() {
        lastSentCommand = Protocol.CMD_SHAKE;
        activityLog("SHAKE", "");
        if (connection.sendCommand(Protocol.CMD_SHAKE)) {
            showStatus("Shaking...");
        }
    }

    private void handleClear() {
        lastSentCommand = Protocol.CMD_CLEAR;
        activityLog("CLEAR", "");
        if (connection.sendCommand(Protocol.CMD_CLEAR)) {
            showStatus("Clearing board...");
        }
    }

    private void handleDisconnect() {
        activityLog("DISCONNECT", "");
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
        if ("GET_FILTERED".equals(lastSentCommand)) {
            appendGetLogFromServerFiltered(lastGetFilterDescription, remainder);
            lastSentCommand = "GET";
            connection.sendCommand(Protocol.CMD_GET);
        } else if ("GET".equals(lastSentCommand)) {
            applyNotesFromGetResponse(remainder);
            lastSentCommand = "GET_PINS";
            connection.sendCommand(Protocol.CMD_GET + " " + Protocol.GET_PINS);
        } else if ("GET_PINS".equals(lastSentCommand)) {
            int pinCount = applyPinsFromGetPinsResponse(remainder);
            if (getWasUserInitiated) {
                showGetSuccess(lastGetNoteCount, pinCount);
                if (!getHadFilters) {
                    appendGetLogFullBoard(lastGetNoteCount, pinCount);
                }
                getWasUserInitiated = false;
                getHadFilters = false;
            } else {
                showStatus("Done.");
            }
            lastSentCommand = "";
        } else if (Protocol.CMD_POST.equals(lastSentCommand) || Protocol.CMD_PIN.equals(lastSentCommand)
                || Protocol.CMD_UNPIN.equals(lastSentCommand) || Protocol.CMD_SHAKE.equals(lastSentCommand)
                || Protocol.CMD_CLEAR.equals(lastSentCommand)) {
            activityLog("Response", "OK");
            showStatus("Done.");
            refreshBoard();
        } else {
            showStatus("OK");
            lastSentCommand = "";
        }
    }

    private void refreshBoard() {
        getWasUserInitiated = false;
        lastSentCommand = "GET";
        connection.sendCommand(Protocol.CMD_GET);
    }

    private int lastGetNoteCount = 0;

    private void applyNotesFromGetResponse(String remainder) {
        if (remainder == null || remainder.trim().isEmpty()) {
            noteWidgets.clear();
            lastGetNoteCount = 0;
            repaintBoard();
            return;
        }
        String[] segments = remainder.split("\\" + Protocol.LIST_SEPARATOR);
        noteWidgets.clear();
        for (String seg : segments) {
            seg = seg.trim();
            if (seg.isEmpty())
                continue;
            String[] parts = seg.split("\\s+", 4);
            if (parts.length >= 3) {
                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    String colour = parts[2];
                    String message = parts.length > 3 ? parts[3] : "";
                    String noteId = x + "_" + y;
                    NoteWidget w = new NoteWidget(noteId, x, y, colour, message, noteWidth, noteHeight);
                    noteWidgets.add(w);
                } catch (NumberFormatException ignored) {
                }
            }
        }
        lastGetNoteCount = noteWidgets.size();
        repaintBoard();
    }

    private int applyPinsFromGetPinsResponse(String remainder) {
        if (remainder == null || remainder.trim().isEmpty()) {
            pinWidgets.clear();
            updateNotePinnedState();
            repaintBoard();
            return 0;
        }
        String[] segments = remainder.split("\\" + Protocol.LIST_SEPARATOR);
        pinWidgets.clear();
        int idx = 0;
        for (String seg : segments) {
            seg = seg.trim();
            if (seg.isEmpty())
                continue;
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
        return pinWidgets.size();
    }

    private java.util.List<Point> getPinsForNote(int noteX, int noteY) {
        java.util.List<Point> points = new ArrayList<>();
        for (PinWidget p : pinWidgets) {
            int px = p.getX();
            int py = p.getY();
            if (px >= noteX && px < noteX + noteWidth && py >= noteY && py < noteY + noteHeight) {
                points.add(new Point(px, py));
            }
        }
        return points;
    }

    private void updateNotePinnedState() {
        for (NoteWidget n : noteWidgets) {
            n.setPins(getPinsForNote(n.getX(), n.getY()));
        }
    }

    private void repaintBoard() {
        boardPanel.removeAll();
        for (NoteWidget w : noteWidgets) {
            w.setBounds(w.getX(), w.getY(), noteWidth, noteHeight);
            boardPanel.add(w, 0);
        }
        for (PinWidget w : pinWidgets) {
            w.setBounds(w.getX(), w.getY(), 10, 10);
            boardPanel.add(w, 0);
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
        activityLog("ERROR", message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setOpaque(false);
        statusLabel.setBackground(null);
    }

    private void showGetSuccess(int noteCount, int pinCount) {
        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(200, 255, 200));
        statusLabel.setForeground(new Color(0, 120, 0));
        statusLabel.setText(" ✓ Board updated — " + noteCount + " note(s), " + pinCount + " pin(s)");
        Timer t = new Timer(2500, e -> {
            statusLabel.setOpaque(false);
            statusLabel.setBackground(null);
            statusLabel.setForeground(Color.BLACK);
            statusLabel.setText("Ready.");
        });
        t.setRepeats(false);
        t.start();
    }

    /** Log entry from server filtered GET response (filtering done on backend). */
    private void appendGetLogFromServerFiltered(String filterDescription, String remainder) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        List<String> noteEntries = new ArrayList<>();
        if (remainder != null && !remainder.trim().isEmpty()) {
            for (String seg : remainder.split("\\" + Protocol.LIST_SEPARATOR)) {
                seg = seg.trim();
                if (seg.isEmpty())
                    continue;
                String[] parts = seg.split("\\s+", 4);
                if (parts.length >= 3) {
                    String msg = parts.length > 3 ? parts[3] : "";
                    if (msg.length() > 15)
                        msg = msg.substring(0, 12) + "...";
                    noteEntries.add("(" + parts[0] + "," + parts[1] + ") " + parts[2] + " \"" + msg + "\"");
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(time).append("] GET (filter: ").append(filterDescription).append(") — ")
                .append(noteEntries.size()).append(" note(s) from server\n");
        if (noteEntries.isEmpty()) {
            sb.append("  (none)\n");
        } else {
            sb.append("  ").append(String.join("; ", noteEntries)).append("\n");
        }
        sb.append("\n");
        getLogArea.append(sb.toString());
        getLogArea.setCaretPosition(getLogArea.getDocument().getLength());
    }

    /** Log entry for full board (no filter) after GET + GET_PINS. */
    private void appendGetLogFullBoard(int noteCount, int pinCount) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(time).append("] GET — ").append(noteCount).append(" note(s), ").append(pinCount)
                .append(" pin(s)\n");
        if (!noteWidgets.isEmpty()) {
            sb.append("  Notes: ");
            for (int i = 0; i < noteWidgets.size(); i++) {
                NoteWidget n = noteWidgets.get(i);
                if (i > 0)
                    sb.append("; ");
                String msg = n.getMessage();
                if (msg == null)
                    msg = "";
                if (msg.length() > 15)
                    msg = msg.substring(0, 12) + "...";
                sb.append("(").append(n.getX()).append(",").append(n.getY()).append(") \"").append(msg).append("\"");
            }
            sb.append("\n");
        }
        if (!pinWidgets.isEmpty()) {
            sb.append("  Pins: ");
            for (int i = 0; i < pinWidgets.size(); i++) {
                PinWidget p = pinWidgets.get(i);
                if (i > 0)
                    sb.append("; ");
                sb.append("(").append(p.getX()).append(",").append(p.getY()).append(")");
            }
            sb.append("\n");
        }
        sb.append("\n");
        getLogArea.append(sb.toString());
        getLogArea.setCaretPosition(getLogArea.getDocument().getLength());
    }

    /**
     * Appends one line to the activity log (UI only). Use for commands and errors.
     */
    private void activityLog(String level, String message) {
        String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String line = "[" + time + "] " + level + " — " + message + "\n";
        getLogArea.append(line);
        getLogArea.setCaretPosition(getLogArea.getDocument().getLength());
    }

    private void paintBoard(Graphics g) {
        g.setColor(BOARD_BG);
        g.fillRect(0, 0, boardWidth, boardHeight);
        g.setColor(BOARD_BORDER);
        g.drawRect(0, 0, boardWidth - 1, boardHeight - 1);
    }
}
