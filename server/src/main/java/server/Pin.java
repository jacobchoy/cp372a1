package server;

// represents a pin on the bulletin board
public class Pin {
    private String id;
    private int x;
    private int y;

    // constructs a new Pin with the specified coordinates
    public Pin(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    // constructs a new Pin with the specified coordinates and a generated ID
    public Pin(int x, int y) {
        this(java.util.UUID.randomUUID().toString(), x, y);
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

    // checks if this pin is at the specified coordinates
    public boolean isAt(int x, int y) {
        return this.x == x && this.y == y;
    }

    // returns a string representation of this pin
    @Override
    public String toString() {
        return String.format("ID: %s, Position: (%d, %d)", id, x, y);
    }
}
