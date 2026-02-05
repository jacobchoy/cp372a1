package shared;

import java.util.Arrays;
import java.util.List;
import java.awt.Color;

// shared colour list for notes (client validation; server sends actual list in handshake)
public class Colours {
    public static final List<String> AVAILABLE_COLOURS = Arrays.asList(
            "RED",
            "BLUE",
            "GREEN");

    public static final Color RED = Color.RED;
    public static final Color GREEN = Color.GREEN;
    public static final Color BLUE = Color.BLUE;

    // return list of colour names
    public static List<String> getAvailableColours() {
        return AVAILABLE_COLOURS;
    }

    // true if colour name is in the list (case-insensitive)
    public static boolean isValidColour(String colourName) {
        if (colourName == null) {
            return false;
        }
        return AVAILABLE_COLOURS.stream()
                .anyMatch(colour -> colour.equalsIgnoreCase(colourName));
    }

    // default colour name
    public static String getDefaultColour() {
        return "RED";
    }

    private Colours() {
    }
}
