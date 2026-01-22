package server;

/**
 * Represents a pin on the bulletin board.
 * 
 * A pin is defined by a coordinate (x, y) and must be placed inside
 * an existing note. A note may have multiple pins, and a note is
 * considered pinned if it has at least one active pin.
 * 
 * @author Team Members
 * @version 1.0
 */
public class Pin {
    private String id;
    private int x;
    private int y;
    private String noteId; // The ID of the note this pin belongs to
    
    /**
     * Constructs a new Pin with the specified properties.
     * 
     * @param id The unique identifier for this pin
     * @param x The x-coordinate of the pin
     * @param y The y-coordinate of the pin
     * @param noteId The ID of the note this pin is associated with
     */
    public Pin(String id, int x, int y, String noteId) {
        // Implementation will go here
    }
    
    /**
     * Gets the unique identifier of this pin.
     * 
     * @return The pin's ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the x-coordinate of this pin.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Gets the y-coordinate of this pin.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Gets the ID of the note this pin is associated with.
     * 
     * @return The note's ID
     */
    public String getNoteId() {
        return noteId;
    }
    
    /**
     * Returns a string representation of this pin.
     * 
     * @return A string containing the pin's properties
     */
    @Override
    public String toString() {
        // Implementation will go here
        return "";
    }
}
