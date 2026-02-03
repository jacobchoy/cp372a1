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
        this.colour = getColourFromName(colour);
        this.message = message;
        this.isPinned = isPinned;
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
        g.fillRect(x, y, width, height);
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
     * Updates the note's message text.
     * 
     * @param message The new message text
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Updates the note's pinned status.
     * 
     * @param isPinned The new pinned status
     */
    public void setPinned(boolean isPinned) {
        this.isPinned = isPinned;
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
            return Colours.WHITE;
        }

        switch (colourName.toLowerCase()) {
            case "red":
                return Colours.RED;
            case "orange":
                return Colours.ORANGE;
            case "yellow":
                return Colours.YELLOW;
            case "green":
                return Colours.GREEN;
            case "blue":
                return Colours.BLUE;
            case "purple":
                return Colours.PURPLE;
            case "pink":
                return Colours.PINK;
            case "gray":
                return Colours.GRAY;
            case "brown":
                return Colours.BROWN;
            default:
                return Colours.WHITE;
        }
    }
}
