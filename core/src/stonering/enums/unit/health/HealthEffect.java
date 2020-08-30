package stonering.enums.unit.health;

import java.util.HashMap;
import java.util.Map;

import stonering.entity.unit.aspects.CreatureStatusIcon;

/**
 * Represents any effect that changes health state of a creature, such as wounds, diseases, buffs etc.
 * Effects apply some delta to some health functions, see {@link HealthFunctionEnum}.
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
}
