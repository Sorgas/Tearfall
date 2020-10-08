package stonering.enums.unit.health;

import java.util.HashMap;
import java.util.Map;

import stonering.entity.unit.aspects.CreatureStatusIcon;

/**
 * Represents any effect that changes health state of a creature, such as wounds, diseases, buffs etc.
 * Effect applies some deltas to any combination of {@link HealthFunctionEnum}, {@link CreatureAttributeEnum} and {@link GameplayStatEnum}.
 * 
 * @author Alexander on 10.08.2020.
 */
public abstract class HealthEffect {
    public final String name; // unique for creature
    public int ticksLeft = -1; // decreases every tick. buff is removed, when reaches zero. -1 for infinite buffs
    public CreatureStatusIcon icon; // optional

    public final Map<CreatureAttributeEnum, Integer> attributeEffects = new HashMap<>();
    public final Map<HealthFunctionEnum, Float> functionEffects = new HashMap<>();
    public final Map<GameplayStatEnum, Float> statEffects = new HashMap<>();

    public HealthEffect(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HealthEffect " + name;
    }
}
