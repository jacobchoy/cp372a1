package server;

// represents a note on the bulletin board
public class Note {
    private String id;
    private int x;
    private int y;
    private String colour;
    private String message;

    // constructs a new Note with the specified properties
    public Note(String id, int x, int y, String colour, String message) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.colour = colour;
        this.message = message;
    }

    // accessors
    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getColour() {
        return colour;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // checks if a point (px, py) lies within this notes boundaries
    public boolean containsPoint(int px, int py, int noteWidth, int noteHeight) {
        return px >= x && px < x + noteWidth && py >= y && py < y + noteHeight;
    }

    // returns a string representation of this note
    @Override
    public String toString() {
        return String.format("ID: %s, Position: (%d, %d), Colour: %s, Message: %s", id, x, y, colour, message);
    }
}
