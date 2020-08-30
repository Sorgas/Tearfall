package stonering.enums.unit.health;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander on 8/8/2020
 */
public enum HealthFunctionEnum {
    CONSCIOUSNESS(true), // overall performance, low values cause unit to fall or die at 0
    BLOOD, // production of blood. High values help resist poisons and recover from blood loss.
    MOTORIC, // All actions performed by hands
    WALKING, // High values increase movement speed
    VISION, // range combat, detection.
    HEARING,
    BREATHING(true),
    ;

    public static final Map<String, HealthFunctionEnum> map = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(value -> map.put(value.toString().toLowerCase(), value));
    }

    public final boolean LETHAL; // loosing cause death


    HealthFunctionEnum(boolean lethal) {
        LETHAL = lethal;
    }

    HealthFunctionEnum() {
        this(false);
    }
}
