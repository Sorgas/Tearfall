package stonering.enums.unit.health;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of creature's attributes.
 *
 * @author Alexander_Kuzyakov on 16.09.2019.
 */
public enum CreatureAttributeEnum {
    STRENGTH("strength"), // melee damage, carry weight
    ENDURANCE("endurance"), // disease resistance
    AGILITY("agility"), // movement speed
    INTELLIGENCE("intelligence"),
    WISDOM("wisdom"),
    CHARISMA("charisma"),
    PERCEPTION("perception"),
    ;
    public static final Map<String, CreatureAttributeEnum> map = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(value -> map.put(value.NAME, value));
    }

    public final String NAME;
    public final String ICON;

    CreatureAttributeEnum(String name) {
        NAME = toString().toLowerCase();
        ICON = name;
    }

    public static CreatureAttributeEnum get(String name) {
        return map.get(name);
    }
}
