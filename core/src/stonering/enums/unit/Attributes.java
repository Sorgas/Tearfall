package stonering.enums.unit;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of creature's attributes.
 *
 * @author Alexander_Kuzyakov on 16.09.2019.
 */
public enum Attributes {
    AGILITY("agility"),
    ENDURANCE("endurance");

    private static Map<String, Attributes> map = new HashMap<>();

    static {
        for (Attributes value : values()) {
            map.put(value.NAME, value);
        }
    }

    public final String NAME;

    Attributes(String name) {
        NAME = name;
    }

    public static Attributes get(String name) {
        return map.get(name);
    }
}
