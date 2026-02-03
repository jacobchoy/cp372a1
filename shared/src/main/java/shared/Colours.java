package shared;

import java.util.Arrays;
import java.util.List;

/**
 * Shared colour list for the Bulletin Board System.
 * 
 * Valid colours are provided by the server at startup (RFC Section 2.2).
 * This shared list may be used for client-side validation; server sends
 * actual list in handshake (RFC Section 8.1).
 *
 * @author Jacob Choy, Jonathan Bilewicz
 * @version 1.0
 */
public class Colours {
    /**
     * Predefined list of available colours for notes.
     * 
     * Colours are specified as strings that can be converted to
     * Color objects by the client GUI.
     */
    public static final List<String> AVAILABLE_COLOURS = Arrays.asList(
        "RED",
        "ORANGE",
        "YELLOW",
        "GREEN",
        "BLUE",
        "PURPLE",
        "PINK",
        "WHITE",
        "GRAY",
        "BROWN"
    );
    
    /**
     * Gets the list of available colour names.
     * 
     * @return A list of colour name strings
     */
    public static List<String> getAvailableColours() {
        return AVAILABLE_COLOURS;
    }
    
    /**
     * Checks if a colour name is valid (exists in the predefined list).
     * 
     * @param colourName The colour name to validate (case-insensitive)
     * @return true if the colour is valid, false otherwise
     */
    public static boolean isValidColour(String colourName) {
        if (colourName == null) {
            return false;
        }
        return AVAILABLE_COLOURS.stream()
                .anyMatch(colour -> colour.equalsIgnoreCase(colourName));
    }
    
    /**
     * Gets the default colour name.
     * 
     * @return The default colour name
     */
    public static String getDefaultColour() {
        return "WHITE";
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Colours() {
        // Utility class - no instantiation
    }
}
