package stonering.entity.unit.aspects.body;

import stonering.entity.unit.Unit;

/**
 * Represents any effect that changes health state of a creature, such as wounds, diseases, buffs etc.
 * Effects apply some delta to some health functions.
 * 
 * @author Alexander on 10.08.2020.
 */
public abstract class HealthEffect {
    
    public abstract void apply(Unit unit);
    
    public abstract void unapply(Unit unit);
}
