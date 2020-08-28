package stonering.entity.unit.aspects.body;

import java.util.HashMap;
import java.util.Map;

import stonering.enums.unit.health.HealthFunctionEnum;

/**
 * Represents any effect that changes health state of a creature, such as wounds, diseases, buffs etc.
 * Effects apply some delta to some health functions.
 * 
 * @author Alexander on 10.08.2020.
 */
public abstract class HealthEffect {
    public final Map<HealthFunctionEnum, Float> functionEffects = new HashMap<>();
    public final Map<String, Float> realEffects = new HashMap<>();
}
