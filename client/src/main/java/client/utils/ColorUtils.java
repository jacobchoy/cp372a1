package client.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for color-related operations.
 * 
 * Provides methods for:
 * - Converting color name strings to Color objects
 * - Validating color names against the predefined list
 * - Getting the list of available colors (valid colours from server handshake, RFC Section 8.1)
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class ColorUtils {
    private static final Map<String, Color> colorMap = new HashMap<>();
    
    /**
     * Initializes the color map with predefined colors.
     * 
     * Colors are loaded from the shared.Colours class.
     */
    static {
        // Implementation will go here
    }
    
    /**
     * Converts a color name string to a Color object.
     * 
     * @param colorName The name of the color (case-insensitive)
     * @return The corresponding Color object, or null if the color is not found
     */
    public static Color getColor(String colorName) {
        // Implementation will go here
        return null;
    }
    
    /**
     * Checks if a color name is valid (exists in the predefined list).
     * 
     * @param colorName The color name to validate
     * @return true if the color is valid, false otherwise
     */
    public static boolean isValidColor(String colorName) {
        // Implementation will go here
        return false;
    }
    
    /**
     * Gets all available color names.
     * 
     * @return An array of all valid color names
     */
    public static String[] getAvailableColors() {
        // Implementation will go here
        return new String[0];
    }
    
    /**
     * Gets the default color to use when a color is not specified.
     * 
     * @return The default Color object
     */
    public static Color getDefaultColor() {
        // Implementation will go here
        return Color.WHITE;
    }
}
