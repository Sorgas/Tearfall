package stonering.entity.unit.aspects.health;


import stonering.entity.Aspect;
import stonering.enums.unit.health.CreatureAttributeEnum;
import stonering.enums.unit.health.HealthEffect;
import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.enums.unit.health.GameplayStatEnum;
import stonering.util.logging.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores {@link CreatureAttributeEnum} values, {@link HealthFunctionEnum} values, {@link GameplayStatEnum} values, and active {@link HealthEffect}s.
 * Gameplay values like move speed and memory are calculated from base value and modifier.
 * Base value is provided by creature type. Modifier provided by formula in {@link GameplayStatEnum}.
 * Formula uses health function values and creature attribute values.
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {
    public final Map<CreatureAttributeEnum, Integer> baseAttributes = new HashMap<>();
    public final Map<CreatureAttributeEnum, Integer> attributes = new HashMap<>();
    public final Map<HealthFunctionEnum, Float> functions = new HashMap<>();

    public final Map<GameplayStatEnum, Float> baseStats = new HashMap<>();
    public final Map<GameplayStatEnum, Float> stats = new HashMap<>();

    public final Map<String, HealthEffect> effects = new HashMap<>();
    public boolean alive;

    public void update(GameplayStatEnum property) {
        stats.put(property, property.FUNCTION.apply(this) * baseStats.get(property)); //
    }

    public void change(CreatureAttributeEnum attribute, int delta) {
        if(!attributes.containsKey(attribute)) Logger.UNITS.logError("unit hasn't attribute" + attribute + " in health aspect");
        attributes.compute(attribute, (attr, previous) -> previous + delta);
    }

    public void change(HealthFunctionEnum function, float delta) {
        if(!functions.containsKey(function)) Logger.UNITS.logError("unit hasn't function" + function + " in health aspect");
        functions.compute(function, (function1, previous) -> previous + delta);
    }

    public void change(GameplayStatEnum stat, float delta) {
        if(!stats.containsKey(stat)) Logger.UNITS.logError("unit hasn't stat" + stat + " in health aspect");
        stats.compute(stat, (property1, previous) -> previous + delta);
    }
}
