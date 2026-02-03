package client.gui;

import client.utils.ColourUtils;

import javax.swing.*;
import java.awt.*;

/**
 * GUI widget representing a note on the bulletin board.
 * 
 * This widget displays:
 * - The note's rectangular shape with its assigned colour
 * - The note's message text
 * - Visual indication if the note is pinned
 * - The note's position on the board
 * RFC Section 4.3: Note has position (x,y), colour, content, and pin status (derived).
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class NoteWidget extends JComponent {
    private String noteId;
    private int x;
    private int y;
    private String colour;
    private String message;
    private boolean isPinned;
    private int width;
    private int height;

    /**
     * Constructs a new NoteWidget.
     *
     * @param noteId   The unique identifier of the note
     * @param x        The x-coordinate of the note's upper-left corner
     * @param y        The y-coordinate of the note's upper-left corner
     * @param colour   The colour of the note
     * @param message  The message text of the note
     * @param isPinned Whether the note is currently pinned
     * @param width    The width of the note
     * @param height   The height of the note
     */
    public NoteWidget(String noteId, int x, int y, String colour, String message,
                      boolean isPinned, int width, int height) {
        this.noteId = noteId;
        this.x = x;
        this.y = y;
        this.colour = colour;
        this.message = message != null ? message : "";
        this.isPinned = isPinned;
        this.width = width;
        this.height = height;
        setOpaque(false);
        setBounds(x, y, width, height);
        setPreferredSize(new Dimension(width, height));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        Color c = ColourUtils.getColour(colour);
        if (c == null) {
            c = ColourUtils.getDefaultColour();
        }
        g2.setColor(c);
        g2.fillRect(0, 0, width, height);
        g2.setColor(Color.BLACK);
        g2.drawRect(0, 0, width - 1, height - 1);
        if (isPinned) {
            g2.setColor(new Color(80, 80, 80));
            g2.fillOval(width - 12, 2, 10, 10);
        }
        g2.setFont(getFont() != null ? getFont() : new Font("SansSerif", Font.PLAIN, 12));
        FontMetrics fm = g2.getFontMetrics();
        int lineHeight = fm.getAscent();
        int yPos = lineHeight + 2;
        String[] lines = message.split("\n");
        for (String line : lines) {
            if (yPos + fm.getDescent() > height) break;
            g2.drawString(line.length() > 20 ? line.substring(0, 17) + "..." : line, 4, yPos);
            yPos += lineHeight + 2;
        }
        g2.dispose();
    }

    public String getNoteId() {
        return noteId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "";
        repaint();
    }

    public void setPinned(boolean isPinned) {
        this.isPinned = isPinned;
        repaint();
    }

    public boolean containsPoint(int px, int py) {
        return px >= x && px < x + width && py >= y && py < y + height;
    }
}
