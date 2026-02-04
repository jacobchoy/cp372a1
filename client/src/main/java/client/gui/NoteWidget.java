// Imports
package client.gui;

import javax.swing.*;
import shared.Colours;
import java.awt.*;

/**
 * GUI widget representing a note on the bulletin board.
 * This widget displays:
 * - The notes rectangular shape with its assigned colour
 * - The notes message text
 * - Visual indication if the note is pinned
 * - The notes position on the board
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

    // new NoteWidget
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

    // Paints the note widget on the board
    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(colour);
        g.fillRect(0, 0, width, height);
        if (message != null && !message.trim().isEmpty()) {
            g.setColor(getTextColor());
            Font font = new Font("SansSerif", Font.PLAIN, Math.max(10, Math.min(width, height) / 6));
            g.setFont(font);
            FontMetrics fm = g.getFontMetrics();
            int padding = 4;
            int textWidth = fm.stringWidth(message);
            String toDraw = message;
            if (textWidth > width - 2 * padding) {
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

        // pin indicator if pinned
        for (Point p : pins) {
            int px = p.x - this.x;
            int py = p.y - this.y;
            Graphics g2 = g.create();
            g2.translate(px, py);
            PinWidget pinWidget = new PinWidget("visual", p.x, p.y, noteId);
            pinWidget.paintComponent(g2);
            g2.dispose();
        }
    }

    // Pick a readable text colour against the note background
    private Color getTextColor() {
        return Color.WHITE; // only using white as we only use RGB
    }

    // Accessors
    public String getNoteId() {
        return noteId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColourName() {
        return colourName != null ? colourName : "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPins(java.util.List<Point> pins) {
        this.pins = pins != null ? new java.util.ArrayList<>(pins) : new java.util.ArrayList<>();
    }

    public boolean isPinned() {
        return !pins.isEmpty();
    }

    public boolean containsPoint(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }

    // Get the colour from the colour name
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
