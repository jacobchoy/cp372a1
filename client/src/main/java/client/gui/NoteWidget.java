package client.gui;

import javax.swing.*;
import java.awt.*;

/**
 * GUI widget representing a note on the bulletin board.
 * 
 * This widget displays:
 * - The note's rectangular shape with its assigned color
 * - The note's message text
 * - Visual indication if the note is pinned
 * - The note's position on the board
 * 
 * @author Team Members
 * @version 1.0
 */
public class NoteWidget extends JComponent {
    private String noteId;
    private int x;
    private int y;
    private String color;
    private String message;
    private boolean isPinned;
    private int width;
    private int height;
    
    /**
     * Constructs a new NoteWidget.
     * 
     * @param noteId The unique identifier of the note
     * @param x The x-coordinate of the note's upper-left corner
     * @param y The y-coordinate of the note's upper-left corner
     * @param color The color of the note
     * @param message The message text of the note
     * @param isPinned Whether the note is currently pinned
     * @param width The width of the note
     * @param height The height of the note
     */
    public NoteWidget(String noteId, int x, int y, String color, String message, 
                     boolean isPinned, int width, int height) {
        // Implementation will go here
    }
    
    /**
     * Paints the note widget on the board.
     * 
     * @param g The Graphics object for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Implementation will go here
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
        // Implementation will go here
    }
    
    /**
     * Updates the note's pinned status.
     * 
     * @param isPinned The new pinned status
     */
    public void setPinned(boolean isPinned) {
        // Implementation will go here
    }
    
    /**
     * Checks if a point lies within this note's boundaries.
     * 
     * @param px The x-coordinate of the point
     * @param py The y-coordinate of the point
     * @return true if the point is inside the note, false otherwise
     */
    public boolean containsPoint(int px, int py) {
        // Implementation will go here
        return false;
    }
    
    /**
     * Converts a color name string to a Color object.
     * 
     * @param colorName The name of the color
     * @return The corresponding Color object, or Color.WHITE if not found
     */
    private Color getColorFromName(String colorName) {
        // Implementation will go here
        return Color.WHITE;
    }
}
