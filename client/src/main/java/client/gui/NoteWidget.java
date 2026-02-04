package client.gui;

import javax.swing.*;
import shared.Colours;
import java.awt.*;

/**
 * GUI widget representing a note on the bulletin board.
 * 
 * This widget displays:
 * - The note's rectangular shape with its assigned colour
 * - The note's message text
 * - Visual indication if the note is pinned
 * - The note's position on the board
 * RFC Section 4.3: Note has position (x,y), colour, content, and pin status
 * (derived).
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class NoteWidget extends JComponent {
    private String noteId;
    private int x;
    private int y;
    private Color colour;
    private String colourName;
    private String message;
    private java.util.List<Point> pins = new java.util.ArrayList<>();
    private int width;
    private int height;

    /**
     * Constructs a new NoteWidget.
     * 
     * @param noteId  The unique identifier of the note
     * @param x       The x-coordinate of the note's upper-left corner
     * @param y       The y-coordinate of the note's upper-left corner
     * @param colour  The colour of the note
     * @param message The message text of the note
     * @param width   The width of the note
     * @param height  The height of the note
     */
    public NoteWidget(String noteId, int x, int y, String colour, String message, int width, int height) {
        this.noteId = noteId;
        this.x = x;
        this.y = y;
        this.colourName = colour == null ? "" : colour.trim();
        this.colour = getColourFromName(colour);
        this.message = message;
        this.width = width;
        this.height = height;
    }

    /**
     * Paints the note widget on the board.
     * 
     * @param g The Graphics object for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(colour);
        g.fillRect(0, 0, width, height);
        // Draw message text on the note
        if (message != null && !message.trim().isEmpty()) {
            g.setColor(getTextColor());
            Font font = new Font("SansSerif", Font.PLAIN, Math.max(10, Math.min(width, height) / 6));
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int padding = 4;
            int textWidth = fm.stringWidth(message);
            String toDraw = message;
            if (textWidth > width - 2 * padding) {
                // Truncate with ellipsis
                for (int i = message.length(); i > 0; i--) {
                    toDraw = message.substring(0, i) + "...";
                    if (fm.stringWidth(toDraw) <= width - 2 * padding)
                        break;
                }
            }
            int x = padding;
            int y = (height + fm.getAscent()) / 2 - 2;
            g.drawString(toDraw, x, y);
        }
        // Border: darker if pinned, or just regular border
        g.setColor(colour.darker());
        if (!pins.isEmpty()) {
            ((Graphics2D) g).setStroke(new BasicStroke(2));
        }
        g.drawRect(0, 0, width - 1, height - 1);
        if (!pins.isEmpty()) {
            ((Graphics2D) g).setStroke(new BasicStroke(1));
        }

        // Visual pin indicator if pinned
        for (Point p : pins) {
            int px = p.x - this.x;
            int py = p.y - this.y;
            Graphics g2 = g.create();
            g2.translate(px, py);
            // Use dummy ID since this is just for visual rendering
            PinWidget pinWidget = new PinWidget("visual", p.x, p.y, noteId);
            pinWidget.paintComponent(g2);
            g2.dispose();
        }
    }

    /** Pick a readable text colour against the note background. */
    private Color getTextColor() {
        int r = colour.getRed(), g = colour.getGreen(), b = colour.getBlue();
        double luminance = (0.299 * r + 0.587 * g + 0.114 * b) / 255;
        return luminance > 0.6 ? Color.BLACK : Color.WHITE;
    }

    /**
     * Gets the note's unique identifier.
     * 
     * @return The note ID
     */
    public String getNoteId() {
        return noteId;
    }

    /**
     * Gets the x-coordinate of the note.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the note.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the note's colour name (e.g. "red", "blue").
     *
     * @return The colour name
     */
    public String getColourName() {
        return colourName != null ? colourName : "";
    }

    /**
     * Gets the note's message text.
     *
     * @return The message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the note's message text.
     * 
     * @param message The new message text
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Updates the note's pinned status with specific pin locations.
     * 
     * @param pins List of points where pins are located (global coordinates)
     */
    public void setPins(java.util.List<Point> pins) {
        this.pins = pins != null ? new java.util.ArrayList<>(pins) : new java.util.ArrayList<>();
    }

    /**
     * Checks if the note is pinned.
     * 
     * @return true if pinned, false otherwise
     */
    public boolean isPinned() {
        return !pins.isEmpty();
    }

    /**
     * Checks if a point lies within this note's boundaries.
     * 
     * @param px The x-coordinate of the point
     * @param py The y-coordinate of the point
     * @return true if the point is inside the note, false otherwise
     */
    public boolean containsPoint(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    /**
     * Converts a colour name string to a Color object.
     * 
     * @param colourName The name of the colour
     * @return The corresponding Color object, or Color.WHITE if not found
     */
    private Color getColourFromName(String colourName) {
        if (colourName == null || colourName.trim().isEmpty()) {
            return Colours.RED;
        }

        switch (colourName.toLowerCase()) {
            case "red":
                return Colours.RED;
            case "green":
                return Colours.GREEN;
            case "blue":
                return Colours.BLUE;
            default:
                return Colours.RED;
        }
    }
}
