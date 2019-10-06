package stonering.enums.unit;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of creature's attributes.
 *
 * @author Alexander_Kuzyakov on 16.09.2019.
 */
public enum AttributesEnum {
    AGILITY("agility"),
    ENDURANCE("endurance");

    private static Map<String, AttributesEnum> map = new HashMap<>();

    static {
        for (AttributesEnum value : values()) {
            map.put(value.NAME, value);
        }
    }

    public final String NAME;

    AttributesEnum(String name) {
        NAME = name;
    }

    public static AttributesEnum get(String name) {
        return map.get(name);
    }
}
