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
 * Stores information of creature's health. Stores general functions of creature's organism.
 * Gameplay values, like move speed and melee damage are calculated from attributes and health functions.
 * attributes give basic value, and health functions modifies this value by %.
 *
 * TODO add calculation of max values based on creature's attributes.
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {

    public final Map<CreatureAttributeEnum, Integer> baseAttributes = new HashMap<>();
    public final Map<CreatureAttributeEnum, Integer> attributes = new HashMap<>();
    public final Map<HealthFunctionEnum, Float> functions = new HashMap<>();

    public final Map<GameplayStatEnum, Float> baseValues = new HashMap<>();
    public final Map<GameplayStatEnum, Float> stats = new HashMap<>();

    public final Map<String, HealthEffect> effects = new HashMap<>();
    public boolean alive;

    /**
     * Applies formula to health state and base value.
     */
    public void update(GameplayStatEnum property) {
        stats.put(property, property.FUNCTION.apply(this) * baseValues.get(property)); //
        //TODO individual formulas for properties
    }

    public void change(CreatureAttributeEnum attribute, int delta) {
        attributes.compute(attribute, (attribute1, previous) ->
                (previous == null ? Logger.UNITS.logError("unit hasn't attribute" + attribute + " in health aspect", 5) : previous) + delta);
    }

    public void change(HealthFunctionEnum function, float delta) {
        functions.compute(function, (function1, previous) ->
                (previous == null ? Logger.UNITS.logError("unit hasn't function" + function + " in health aspect", 5) : previous) + delta);
    }

    public void change(GameplayStatEnum stat, float delta) {
        stats.compute(stat, (property1, previous) ->
                (previous == null ? Logger.UNITS.logError("unit hasn't stat" + stat + " in health aspect", 5) : previous) + delta);
    }
}
