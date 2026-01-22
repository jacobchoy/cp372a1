package shared;

import java.util.Arrays;
import java.util.List;

/**
 * Shared color list for the Bulletin Board System.
 * 
 * This class defines the predefined list of colors that can be used
 * for notes. Both client and server should use this list to ensure
 * consistency.
 * 
 * @author Team Members
 * @version 1.0
 */
public class Colours {
    /**
     * Predefined list of available colors for notes.
     * 
     * Colors are specified as strings that can be converted to
     * Color objects by the client GUI.
     */
    public static final List<String> AVAILABLE_COLORS = Arrays.asList(
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
     * Gets the list of available color names.
     * 
     * @return A list of color name strings
     */
    public static List<String> getAvailableColors() {
        return AVAILABLE_COLORS;
    }
    
    /**
     * Checks if a color name is valid (exists in the predefined list).
     * 
     * @param colorName The color name to validate (case-insensitive)
     * @return true if the color is valid, false otherwise
     */
    public static boolean isValidColor(String colorName) {
        if (colorName == null) {
            return false;
        }
        return AVAILABLE_COLORS.stream()
                .anyMatch(color -> color.equalsIgnoreCase(colorName));
    }
    
    /**
     * Gets the default color name.
     * 
     * @return The default color name
     */
    public static String getDefaultColor() {
        return "WHITE";
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private Colours() {
        // Utility class - no instantiation
    }
}
