package stonering.enums.unit.health;

import static stonering.enums.unit.health.CreatureAttributeEnum.*;
import static stonering.enums.unit.health.HealthFunctionEnum.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import stonering.entity.unit.aspects.health.HealthAspect;

/**
 * Gameplay values of a creature, like move speed and attack delay modifier.
 * Function returns multiplier for the base value.
 * Stores functions and attributes that affect particular stat, and maps them to all stats they affect.
 * Also stores base default values for creatures. Default values can be overridden with creature type (json).
 *
 * @author Alexander on 8/30/2020
 */
public enum GameplayStatEnum {
    MOVEMENT_SPEED(3, health -> health.functions.get(WALKING) * health.functions.get(CONSCIOUSNESS), Arrays.asList(WALKING, CONSCIOUSNESS), null),
    ATTACK_SPEED(1, health -> (1 + 0.03f * health.attributes.get(AGILITY)) * health.functions.get(MOTORIC) * health.functions.get(CONSCIOUSNESS), Arrays.asList(MOTORIC, CONSCIOUSNESS), Arrays.asList(AGILITY)),
    // should be multiplied by skill for each action
    WORK_SPEED(1, health -> health.functions.get(CONSCIOUSNESS) * health.functions.get(MOTORIC), Arrays.asList(MOTORIC, CONSCIOUSNESS), null),
    ;

    public static final Map<String, GameplayStatEnum> map = new HashMap<>();
    public static final Map<HealthFunctionEnum, Set<GameplayStatEnum>> functionsToStats = new HashMap<>();
    public static final Map<CreatureAttributeEnum, Set<GameplayStatEnum>> attributesToStats = new HashMap<>();

    static {
        Arrays.stream(values()).forEach(stat -> {
            map.put(stat.toString().toLowerCase(), stat);
            stat.functions.stream()
                    .map(function -> functionsToStats.computeIfAbsent(function, func -> new HashSet<>()))
                    .forEach(set -> set.add(stat));
            stat.attributes.stream()
                    .map(attribute -> attributesToStats.computeIfAbsent(attribute, attr -> new HashSet<>()))
                    .forEach(set -> set.add(stat));
        });
    }

    public final float DEFAULT;
    public final Function<HealthAspect, Float> FUNCTION;
    private final List<HealthFunctionEnum> functions;
    private final List<CreatureAttributeEnum> attributes;

    GameplayStatEnum(float humanDefault, Function<HealthAspect, Float> function, List<HealthFunctionEnum> functions, List<CreatureAttributeEnum> attributes) {
        DEFAULT = humanDefault;
        FUNCTION = function;
        this.functions = functions == null ? Collections.emptyList() : functions;
        this.attributes = attributes == null ? Collections.emptyList() : attributes;
    }

    public static Set<GameplayStatEnum> collectProperties(HealthEffect effect) {
        return Stream.concat(
                effect.functionEffects.keySet().stream().map(functionsToStats::get),
                effect.attributeEffects.keySet().stream().map(attributesToStats::get)
        )
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
