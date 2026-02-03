package server;

/**
 * Represents a note on the bulletin board.
 * 
 * RFC Section 4.3: Position (x,y), Dimensions (static width/height from server
 * startup),
 * Colour (from valid list), Content (string; MAY contain spaces), Pin status
 * (derived from pins).
 * All notes are initially unpinned when posted.
 *
 * @author Jacob Choy
 * @version 1.0
 */
public class Note {
    private String id;
    private int x;
    private int y;
    private String colour;
    private String message;

    /**
     * Constructs a new Note with the specified properties.
     * 
     * @param id      The unique identifier for this note
     * @param x       The x-coordinate of the upper-left corner
     * @param y       The y-coordinate of the upper-left corner
     * @param colour  The colour of the note (from predefined list)
     * @param message The text content of the note
     */
    public Note(String id, int x, int y, String colour, String message) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.colour = colour;
        this.message = message;
    }

    /**
     * Gets the unique identifier of this note.
     * 
     * @return The note's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the x-coordinate of the note's upper-left corner.
     * 
     * @return The x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the note's upper-left corner.
     * 
     * @return The y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Gets the colour of this note.
     * 
     * @return The colour name
     */
    public String getColour() {
        return colour;
    }

    /**
     * Gets the message content of this note.
     * 
     * @return The note's message text
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of this note.
     * 
     * @param message The new message text
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Checks if a point (px, py) lies within this note's boundaries.
     * 
     * @param px         The x-coordinate of the point
     * @param py         The y-coordinate of the point
     * @param noteWidth  The width of the note
     * @param noteHeight The height of the note
     * @return true if the point is inside the note, false otherwise
     */
    public boolean containsPoint(int px, int py, int noteWidth, int noteHeight) {
        return px >= x && px < x + noteWidth && py >= y && py < y + noteHeight;
    }

    /**
     * Returns a string representation of this note.
     * 
     * @return A string containing the note's properties
     */
    @Override
    public String toString() {
        return String.format("ID: %s, Position: (%d, %d), Colour: %s, Message: %s", id, x, y, colour, message);
    }
}
