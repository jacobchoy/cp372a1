// imports
package client.gui;

import javax.swing.*;
import java.awt.*;

// gui widget representing a pin on the bulletin board
public class PinWidget extends JComponent {
    private static final int PIN_SIZE = 10;

    private final String pinId;
    private final int x;
    private final int y;
    private final String noteId;

    // constructs a new PinWidget
    public PinWidget(String pinId, int x, int y, String noteId) {
        this.pinId = pinId;
        this.x = x;
        this.y = y;
        this.noteId = noteId;
        setOpaque(false);
        setSize(PIN_SIZE, PIN_SIZE);
        setPreferredSize(new Dimension(PIN_SIZE, PIN_SIZE));
    }

    // paints the pin widget on the board
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int s = PIN_SIZE;
        // red pin head so it's obvious on any note
        g2.setColor(new Color(200, 50, 50));
        g2.fillOval(0, 0, s, s);
        g2.setColor(new Color(120, 25, 25));
        g2.drawOval(0, 0, s - 1, s - 1);
        g2.setColor(new Color(255, 180, 180));
        g2.fillOval(2, 2, 3, 3);
        g2.dispose();
    }

    // accessors
    public String getPinId() {
        return pinId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getNoteId() {
        return noteId;
    }
}
