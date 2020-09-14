package stonering.enums.unit.health;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Maps {@link HealthFunctionEnum} and {@link CreatureAttributeEnum} to dependent {@link GameplayStatEnum}.
 *
 * @author Alexander on 8/30/2020
 */
public class HealthParameterMapping {
    public final Map<HealthFunctionEnum, Set<GameplayStatEnum>> functions;
    public final Map<CreatureAttributeEnum, Set<GameplayStatEnum>> attributes;

    public HealthParameterMapping() {
        functions = new HashMap<>();
        attributes = new HashMap<>();
        //TODO fill mapping
    }

    /**
     * Gathers all health properties, affected by health effect.
     */
    public Set<GameplayStatEnum> collectProperties(HealthEffect effect) {
        Set<GameplayStatEnum> properties = effect.attributeEffects.keySet().stream()
                .map(attributes::get)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        effect.functionEffects.keySet().stream()
                .map(functions::get)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .forEach(properties::add);
        return properties;
    }

    private void addFunction(HealthFunctionEnum function, GameplayStatEnum property) {
        functions.computeIfAbsent(function, func -> new HashSet<>()).add(property);
    }

    private void addAttribute(CreatureAttributeEnum attribute, GameplayStatEnum property) {
        attributes.computeIfAbsent(attribute, attr -> new HashSet<>()).add(property);
    }
}
