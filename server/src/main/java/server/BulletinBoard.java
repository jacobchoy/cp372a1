package server;

import java.util.List;

/**
 * Thread-safe bulletin board that manages notes and pins.
 * 
 * This class maintains collections of Note and Pin objects and provides
 * synchronized access methods to ensure thread safety when multiple clients
 * access the board concurrently.
 * 
 * RFC Section 4.2: Board has fixed dimensions; notes MUST lie completely within
 * boundaries;
 * notes MAY partially overlap but MUST NOT completely overlap (Section 3).
 * RFC Section 10.2: Shared data protection; modifications must prevent data
 * races.
 *
 * @author Jacob Choy
 * @version 1.0
 */
public class BulletinBoard {
    private final int boardWidth;
    private final int boardHeight;
    private final int noteWidth;
    private final int noteHeight;

    /**
     * Constructs a new BulletinBoard with specified dimensions.
     * 
     * The board dimensions are defined at server startup (RFC Section 2.2)
     * and passed to this constructor from ServerMain.main().
     * 
     * These dimensions are stored in the final fields and remain constant
     * for the lifetime of the server (RFC Section 4.2).
     * 
     * @param boardWidth  The width of the bulletin board (from command-line
     *                    args[1])
     * @param boardHeight The height of the bulletin board (from command-line
     *                    args[2])
     * @param noteWidth   The fixed width of all notes (from command-line args[3])
     * @param noteHeight  The fixed height of all notes (from command-line args[4])
     */
    public BulletinBoard(int boardWidth, int boardHeight, int noteWidth, int noteHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.noteWidth = noteWidth;
        this.noteHeight = noteHeight;
    }

    /**
     * Adds a note to the bulletin board.
     * 
     * Validates that the note lies completely within the board boundaries
     * and does not completely overlap with an existing note.
     * 
     * @param note The note to add
     * @return true if the note was successfully added, false otherwise
     */
    public boolean addNote(Note note) {
        // Out of bounds check
        if (!isValidNotePosition(note.getX(), note.getY())) {
            return false;
        }
        // Overlap check
        for (Note existingNote : getNotes()) {
            if (notesCompletelyOverlap(note, existingNote)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a note from the bulletin board by its ID.
     * 
     * Also removes all pins associated with the note.
     * 
     * @param noteId The unique identifier of the note to remove
     * @return true if the note was found and removed, false otherwise
     */
    public boolean removeNote(String noteId) {
        if (noteId != null) {
            for (Note note : getNotes()) {
                if (note.getId().equals(noteId)) {
                    note.remove(note);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Retrieves all notes currently on the bulletin board.
     * 
     * @return A list of all notes (defensive copy for thread safety)
     */
    public List<Note> getNotes() {
        // Implementation will go here
        return null;
    }

    /**
     * Retrieves a specific note by its ID.
     * 
     * @param noteId The unique identifier of the note
     * @return The note with the given ID, or null if not found
     */
    public Note getNote(String noteId) {
        // Implementation will go here
        return null;
    }

    /**
     * Adds a pin to the bulletin board at the specified coordinates.
     * 
     * RFC Section 7.3: PIN x y places a pin at (x, y).
     * All notes covering that coordinate become pinned.
     * 
     * @param x The x-coordinate where to place the pin
     * @param y The y-coordinate where to place the pin
     * @return true if the pin was successfully added (coordinate is inside at least
     *         one note), false otherwise
     */
    public boolean addPin(int x, int y) {
        // Implementation will go here
        return false;
    }

    /**
     * Removes a pin from the bulletin board at the specified coordinates.
     * 
     * RFC Section 7.4: UNPIN x y removes one pin at (x, y).
     * 
     * @param x The x-coordinate of the pin to remove
     * @param y The y-coordinate of the pin to remove
     * @return true if a pin was found and removed at that coordinate, false
     *         otherwise
     */
    public boolean removePin(int x, int y) {
        // Implementation will go here
        return false;
    }

    /**
     * Removes all unpinned notes from the board.
     * 
     * RFC Section 7.5: SHAKE removes all unpinned notes.
     * The operation MUST be atomic.
     * 
     * @return The number of notes removed
     */
    public int shake() {
        // Implementation will go here
        return 0;
    }

    /**
     * Gets all notes that contain the specified coordinate.
     * 
     * RFC Section 7.2.2: GET contains=x y returns notes covering that coordinate.
     * 
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return A list of notes that contain the coordinate
     */
    public List<Note> getNotesContaining(int x, int y) {
        // Implementation will go here
        return null;
    }

    /**
     * Gets all notes with the specified colour.
     *
     * RFC Section 7.2.2: GET color=&lt;colour&gt; returns notes with that colour.
     *
     * @param colour The colour to filter by
     * @return A list of notes with the specified colour
     */
    public List<Note> getNotesByColour(String colour) {
        // Implementation will go here
        return null;
    }

    /**
     * Gets all notes whose content contains the specified substring.
     * 
     * RFC Section 7.2.2: GET refersTo=<substring> returns notes containing the
     * substring.
     * 
     * @param substring The substring to search for
     * @return A list of notes containing the substring
     */
    public List<Note> getNotesByContent(String substring) {
        // Implementation will go here
        return null;
    }

    /**
     * Gets all pins at the specified coordinate.
     * 
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return A list of pins at that coordinate (may be empty)
     */
    public List<Pin> getPinsAt(int x, int y) {
        // Implementation will go here
        return null;
    }

    /**
     * Removes all notes and all pins from the board.
     *
     * RFC Section 7.6: CLEAR removes all notes and all pins. The operation MUST be
     * atomic (Section 10.3).
     */
    public void clear() {
        // Implementation will go here
    }

    /**
     * Retrieves all pins currently on the bulletin board.
     *
     * @return A list of all pins (defensive copy for thread safety)
     */
    public List<Pin> getPins() {
        // Implementation will go here
        return null;
    }

    /**
     * Retrieves a specific pin by its ID (internal use; protocol identifies pins by
     * coordinates).
     * 
     * @param pinId The unique identifier of the pin
     * @return The pin with the given ID, or null if not found
     */
    public Pin getPin(String pinId) {
        // Implementation will go here
        return null;
    }

    /**
     * Gets the width of the bulletin board.
     * 
     * @return The board width
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Gets the height of the bulletin board.
     * 
     * @return The board height
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Gets the fixed width of notes.
     * 
     * @return The note width
     */
    public int getNoteWidth() {
        return noteWidth;
    }

    /**
     * Gets the fixed height of notes.
     * 
     * @return The note height
     */
    public int getNoteHeight() {
        return noteHeight;
    }

    /**
     * Validates if a note's position and dimensions are within board boundaries.
     *
     * RFC Section 4.2: All notes MUST lie completely within the board boundaries.
     *
     * @param x The x-coordinate of the note's upper-left corner
     * @param y The y-coordinate of the note's upper-left corner
     * @return true if the note lies completely within the board, false otherwise
     */
    private boolean isValidNotePosition(int x, int y) {
        return x >= 0 && y >= 0 && x + noteWidth <= boardWidth && y + noteHeight <= boardHeight;
    }

    /**
     * Checks if two notes completely overlap (occupy the exact same region).
     *
     * RFC Section 3: Complete overlap = same rectangular region and coordinates.
     *
     * @param note1 The first note
     * @param note2 The second note
     * @return true if the notes completely overlap, false otherwise
     */
    private boolean notesCompletelyOverlap(Note note1, Note note2) {
        return note1.getX() == note2.getX() && note1.getY() == note2.getY() && note1.getId().equals(note2.getId());
    }

    /**
     * Checks if a pin coordinate lies within a note's boundaries.
     *
     * RFC Section 4.4: A pin MAY be placed only at a coordinate inside at least one
     * existing note.
     *
     * @param pinX The x-coordinate of the pin
     * @param pinY The y-coordinate of the pin
     * @param note The note to check against
     * @return true if the pin is inside the note, false otherwise
     */
    private boolean isPinInsideNote(int pinX, int pinY, Note note) {
        return pinX >= note.getX() && pinX < note.getX() + noteWidth && pinY >= note.getY()
                && pinY < note.getY() + noteHeight;
    }
}
