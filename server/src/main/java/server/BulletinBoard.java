package server;

import java.util.ArrayList;
import java.util.List;

// bulletin board that manages notes and pins
public class BulletinBoard {
    private final int boardWidth;
    private final int boardHeight;
    private final int noteWidth;
    private final int noteHeight;

    // List of all notes and pins on the board
    private final List<Note> notes;
    private final List<Pin> pins;

    // constructs a new BulletinBoard
    public BulletinBoard(int boardWidth, int boardHeight, int noteWidth, int noteHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.noteWidth = noteWidth;
        this.noteHeight = noteHeight;
        this.notes = new ArrayList<>();
        this.pins = new ArrayList<>();
    }

    // adds a note to the bulletin board
    public synchronized boolean addNote(Note note) {
        // oob check
        if (!isValidNotePosition(note.getX(), note.getY())) {
            return false;
        }
        // overlap check
        for (Note existingNote : notes) {
            if (notesCompletelyOverlap(note, existingNote)) {
                return false;
            }
        }
        notes.add(note);
        return true;
    }

    // removes a note from the bulletin board by its ID
    public synchronized boolean removeNote(String noteId) {
        if (noteId != null) {
            for (Note note : notes) {
                if (note.getId().equals(noteId)) {
                    notes.remove(note);
                    return true;
                }
            }
        }
        return false;
    }

    // retrieves all notes currently on the bulletin board
    public synchronized List<Note> getNotes() {
        return new ArrayList<>(notes);
    }

    // retrieves a specific note by its ID
    public Note getNote(String noteId) {
        if (noteId != null) {
            for (Note note : getNotes()) {
                if (note.getId().equals(noteId)) {
                    return note;
                }
            }
        }
        return null;
    }

    // adds a pin to the bulletin board at the specified coordinates
    public synchronized boolean addPin(int x, int y) {
        for (Note note : notes) {
            if (isPinInsideNote(x, y, note)) {
                pins.add(new Pin(x, y));
                return true;
            }
        }
        return false;
    }

    // removes a pin from the bulletin board at the specified coordinates
    public synchronized boolean removePin(int x, int y) {
        java.util.Iterator<Pin> iterator = pins.iterator();
        while (iterator.hasNext()) {
            Pin pin = iterator.next();
            if (pin.getX() == x && pin.getY() == y) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    // removes all unpinned notes from the board
    public synchronized int shake() {
        List<Note> toRemove = new ArrayList<>();
        for (Note note : notes) {
            boolean isPinned = false;
            for (Pin pin : pins) {
                if (isPinInsideNote(pin.getX(), pin.getY(), note)) {
                    isPinned = true;
                    break;
                }
            }
            if (!isPinned) {
                toRemove.add(note);
            }
        }
        notes.removeAll(toRemove);
        return toRemove.size();
    }

    // gets all notes that contain the coordinate
    public synchronized List<Note> getNotesContaining(int x, int y) {
        List<Note> result = new ArrayList<>();
        for (Note note : notes) {
            if (note.containsPoint(x, y, noteWidth, noteHeight)) {
                result.add(note);
            }
        }
        return result;
    }

    // gets all notes with the colour
    public synchronized List<Note> getNotesByColour(String colour) {
        List<Note> result = new ArrayList<>();
        for (Note note : notes) {
            if (note.getColour().equals(colour)) {
                result.add(note);
            }
        }
        return result;
    }

    // gets all notes whose content contains the substring
    public synchronized List<Note> getNotesByContent(String substring) {
        List<Note> result = new ArrayList<>();
        for (Note note : notes) {
            if (note.getMessage().contains(substring)) {
                result.add(note);
            }
        }
        return result;
    }

    // gets all pins at the coordinate
    public synchronized List<Pin> getPinsAt(int x, int y) {
        List<Pin> result = new ArrayList<>();
        for (Pin pin : pins) {
            if (pin.getX() == x && pin.getY() == y) {
                result.add(pin);
            }
        }
        return result;
    }

    // removes all notes and all pins from the board
    public synchronized void clear() {
        notes.clear();
        pins.clear();
    }

    // retrieves all pins currently on the bulletin board
    public synchronized List<Pin> getPins() {
        return new ArrayList<>(pins);
    }

    // retrieves a specific pin by its ID
    public synchronized Pin getPin(String pinId) {
        for (Pin pin : pins) {
            if (pin.getId().equals(pinId)) {
                return pin;
            }
        }
        return null;
    }

    // accessors
    public synchronized int getBoardWidth() {
        return boardWidth;
    }

    public synchronized int getBoardHeight() {
        return boardHeight;
    }

    public synchronized int getNoteWidth() {
        return noteWidth;
    }

    public synchronized int getNoteHeight() {
        return noteHeight;
    }

    // validates if a notes position and dimensions are within board boundaries
    private synchronized boolean isValidNotePosition(int x, int y) {
        return x >= 0 && y >= 0 && x + noteWidth <= boardWidth && y + noteHeight <= boardHeight;
    }

    // checks if two notes completely overlap
    private synchronized boolean notesCompletelyOverlap(Note note1, Note note2) {
        return note1.getX() == note2.getX() && note1.getY() == note2.getY();
    }

    // checks if a pin coordinate lies within a notes boundaries
    private synchronized boolean isPinInsideNote(int pinX, int pinY, Note note) {
        return pinX >= note.getX() && pinX < note.getX() + noteWidth && pinY >= note.getY()
                && pinY < note.getY() + noteHeight;
    }
}
