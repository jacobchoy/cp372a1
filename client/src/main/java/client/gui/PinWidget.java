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
    private String pinId;
    private int x;
    private int y;
    private String noteId; // The note this pin belongs to
    
    /**
     * Constructs a new PinWidget.
     * 
     * @param pinId The unique identifier of the pin
     * @param x The x-coordinate of the pin
     * @param y The y-coordinate of the pin
     * @param noteId The ID of the note this pin belongs to
     */
    public PinWidget(String pinId, int x, int y, String noteId) {
        // Implementation will go here
    }
    
    /**
     * Paints the pin widget on the board.
     * 
     * @param g The Graphics object for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        // Implementation will go here
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
     * Gets the x-coordinate of the pin.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate of the pin.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Gets the ID of the note this pin belongs to.
     * 
     * @return The note ID
     */
    public String getNoteId() {
        return noteId;
    }
}
