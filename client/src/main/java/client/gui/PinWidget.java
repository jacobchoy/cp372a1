package client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * GUI widget representing a pin on the bulletin board.
 * 
 * This widget displays a small visual indicator (e.g., a circle or icon)
 * at the pin's coordinate position. Pins are typically displayed on top
 * of notes to indicate that a note is pinned.
 * RFC Section 4.4: Pin is defined by coordinate (x, y); protocol uses coordinates.
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class PinWidget extends JComponent {
    /** Default size of the pin head in pixels. */
    private static final int PIN_SIZE = 10;

    private final String pinId;
    private final int x;
    private final int y;
    private final String noteId;

    /**
     * Constructs a new PinWidget.
     *
     * @param pinId  The unique identifier of the pin
     * @param x      The x-coordinate of the pin
     * @param y      The y-coordinate of the pin
     * @param noteId The ID of the note this pin belongs to (may be null)
     */
    public PinWidget(String pinId, int x, int y, String noteId) {
        this.pinId = pinId;
        this.x = x;
        this.y = y;
        this.noteId = noteId;
        setOpaque(false);
        setSize(PIN_SIZE, PIN_SIZE);
        setPreferredSize(new Dimension(PIN_SIZE, PIN_SIZE));
    }

    /**
     * Paints the pin widget on the board.
     * Draws a small circle (pin head) with a dark fill and lighter highlight.
     *
     * @param g The Graphics object for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int s = PIN_SIZE;
        // Red pin head so it's obvious on any note
        g2.setColor(new Color(200, 50, 50));
        g2.fillOval(0, 0, s, s);
        g2.setColor(new Color(120, 25, 25));
        g2.drawOval(0, 0, s - 1, s - 1);
        g2.setColor(new Color(255, 180, 180));
        g2.fillOval(2, 2, 3, 3);
        g2.dispose();
    }

    /**
     * Gets the pin's unique identifier.
     *
     * @return The pin ID
     */
    public String getPinId() {
        return pinId;
    }

    /**
     * Gets the x-coordinate of the pin on the board.
     *
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the pin on the board.
     *
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the ID of the note this pin belongs to.
     *
     * @return The note ID, or null if not associated
     */
    public String getNoteId() {
        return noteId;
    }
}
