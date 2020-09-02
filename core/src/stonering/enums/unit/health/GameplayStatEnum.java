package stonering.enums.unit.health;

import static stonering.enums.unit.health.CreatureAttributeEnum.*;
import static stonering.enums.unit.health.HealthFunctionEnum.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import stonering.entity.unit.aspects.health.HealthAspect;

/**
 * Gameplay values of a creature, like move speed and attack delay modifier.
 * Function returns multiplier for the base value.
 * Also stores base default values for creatures. Default values can be overrode with creature type (json).
 *
 * @author Alexander on 8/30/2020
 */
public enum GameplayStatEnum {
    MOVEMENT_SPEED(3, health -> health.functions.get(WALKING) * health.functions.get(CONSCIOUSNESS)),
    ATTACK_SPEED(1, health -> (1 + 0.03f * health.attributes.get(AGILITY)) * health.functions.get(MOTORIC) * health.functions.get(CONSCIOUSNESS)),
    WORK_SPEED(1, health -> health.functions.get(MOTORIC) * health.functions.get(CONSCIOUSNESS)), // additional components in action
    ;

    public static final Map<String, GameplayStatEnum> map = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(value -> map.put(value.toString().toLowerCase(), value));
    }

    public final float DEFAULT;
    public final Function<HealthAspect, Float> FUNCTION;

    GameplayStatEnum(float humanDefault, Function<HealthAspect, Float> function) {
        DEFAULT = humanDefault;
        FUNCTION = function;
    }
}
