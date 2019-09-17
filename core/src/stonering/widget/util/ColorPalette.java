package stonering.widget.util;

import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;

/**
 * Stores all colors for sprite tinting.
 *
 * @author Alexander on 07.10.2018.
 */
public class ColorPalette {
    private static ColorPalette instance;
    private HashMap<String, Color> colorMap;

    public static ColorPalette getInstance() {
        return instance != null ? instance : (instance = new ColorPalette());
    }

    private ColorPalette() {
        colorMap = new HashMap<>();
        //TODO load palette from config
    }

    /**
     * Returns color represented by string from cache.
     * Creates new color, if it is new.
     *
     * @param s String like FFFFFFFF
     * @return
     */
    public Color getColor(String s) {
        if (colorMap.keySet().contains(s)) {
            return colorMap.get(s);
        } else {
            return createColor(s);
        }
    }

    private Color createColor(String s) {
        Color color;
        if (s.chars().allMatch(character -> Character.digit(character, 16) > 0)) {
            color = new Color(new Integer(s));
        } else {
            color = CreateMockColor();
        }
        colorMap.put(s, color);
        return color;
    }

    /**
     * Mock color to indicate errors in configs
     *
     * @return
     */
    private Color CreateMockColor() {
        return new Color(0xFF00BF00);
    }
}
