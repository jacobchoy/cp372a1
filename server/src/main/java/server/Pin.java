package server;

/**
 * Represents a pin on the bulletin board.
 * 
 * RFC Section 4.4: A pin is defined by a coordinate (x, y) on the board.
 * A pin MAY be placed only at a coordinate that lies inside at least one existing note.
 * A single pin coordinate MAY be associated with multiple notes if those notes partially overlap.
 * 
 * Protocol identifies pins by coordinates (PIN x y, UNPIN x y); internal ID is for tracking only.
 *
 * @author Jacob Choy
 * @version 1.0
 */
public class Pin {
    private String id; // Internal ID for tracking (not used in protocol)
    private int x;
    private int y;
    
    /**
     * Constructs a new Pin with the specified coordinates.
     * 
     * RFC Section 7.3: PIN x y - coordinates are used to identify pins in the protocol.
     * 
     * @param id Internal identifier for tracking (optional, may be generated)
     * @param x The x-coordinate of the pin
     * @param y The y-coordinate of the pin
     */
    public Pin(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    
    /**
     * Gets the internal identifier of this pin.
     * 
     * Note: The protocol uses coordinates, not IDs, but IDs may be useful for internal tracking.
     * 
     * @return The pin's internal ID
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
     * Checks if this pin is at the specified coordinates.
     * 
     * RFC Section 7.4: UNPIN x y removes pins by coordinate, not ID.
     * 
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return true if this pin is at the specified coordinates
     */
    public boolean isAt(int x, int y) {
        return this.x == x && this.y == y;
    }
    
    /**
     * Returns a string representation of this pin.
     * 
     * @return A string containing the pin's properties
     */
    @Override
    public String toString() {
        return String.format("ID: %s, Position: (%d, %d)", id, x, y);
    }
}
