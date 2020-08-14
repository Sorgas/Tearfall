package stonering.enums.unit;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of creature's attributes.
 *
 * @author Alexander_Kuzyakov on 16.09.2019.
 */
public enum CreatureAttributesEnum {
    STRENGTH("strength"),
    AGILITY("agility"),
    INTELLIGENCE("intelligence"),
    CHARISMA("charisma"),
    PERCEPTION("perception"),
    WISDOM("wisdom"),
    ENDURANCE("endurance");

    private static Map<String, CreatureAttributesEnum> map = new HashMap<>();

    static {
        for (CreatureAttributesEnum value : values()) {
            map.put(value.NAME, value);
        }
    }

    public final String NAME;

    CreatureAttributesEnum(String name) {
        NAME = name;
    }

    public static CreatureAttributesEnum get(String name) {
        return map.get(name);
    }
}
