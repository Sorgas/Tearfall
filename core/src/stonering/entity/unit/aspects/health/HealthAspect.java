package stonering.entity.unit.aspects.health;


import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.unit.health.NeedEnum;
import stonering.game.model.system.unit.CreatureHealthSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores health condition of a unit. See {@link CreatureHealthSystem}
 * Properties are values that can influence creature in a various way (speed, performance, etc).
 * Default values are 0, and only buff modifiers are stored. Buffs apply additive changes, which is removed when buff fades (10% == 0.1f).
 *
 * Parameters are values of creatures health conditions, like hunger of fatigue. Parameters can produce buffs which influence properties.
 * TODO add calculation of max values based on creature's attributes.
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {
    public final Map<String, Float> properties; // make properties enumeration
    public boolean alive;
    
    public HealthAspect(Entity entity) {
        super(entity);
        properties = new HashMap<>();
    }
}
