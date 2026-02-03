package client.utils;

import shared.Colours;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for GUI colour operations.
 * 
 * Converts colour name strings (from {@link shared.Colours}) to AWT Color for rendering.
 * For the list of valid colours use {@link shared.Colours#getAvailableColours()};
 * for the default colour name use {@link shared.Colours#getDefaultColour()}.
 *
 * @author Jonathan Bilewicz
 * @version 1.0
 */
public class ColourUtils {
    private static final Map<String, Color> colourMap = new HashMap<>();
    
    /**
     * Initializes the colour map with predefined colours.
     * Keys are lowercase for case-insensitive lookup.
     */
    static {
        colourMap.put("red", Color.RED);
        colourMap.put("green", Color.GREEN);
        colourMap.put("blue", Color.BLUE);
        colourMap.put("yellow", Color.YELLOW);
        colourMap.put("purple", Color.MAGENTA);
        colourMap.put("pink", Color.PINK);
        colourMap.put("white", Color.WHITE);
        colourMap.put("gray", Color.GRAY);
        colourMap.put("brown", Color.BROWN);
        colourMap.put("orange", Color.ORANGE);

    }

    /**
     * Converts a colour name string to a Color object.
     *
     * @param colourName The name of the colour (case-insensitive)
     * @return The corresponding Color object, or null if the colour is not found
     */
    public static Color getColour(String colourName) {
        if (colourName == null || colourName.trim().isEmpty()) {
            return null;
        }
        return colourMap.get(colourName.trim().toLowerCase());
    }
    
    /**
     * Gets the default AWT Color when a colour is not specified (converts
     * {@link shared.Colours#getDefaultColour()} to a Color for painting).
     * 
     * @return The default Color object
     */
    public static Color getDefaultColour() {
        Color c = getColour(Colours.getDefaultColour());
        return c != null ? c : Color.WHITE;
    }
}
