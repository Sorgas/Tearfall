package stonering.entity.unit.aspects.health;


import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.system.units.CreatureHealthSystem;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores health condition of a unit. See {@link CreatureHealthSystem}
 * TODO add calculation of max values based on creature's attributes.
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {
    public final Map<String, Float> properties;

    public float fatigue = 0;
    public float maxFatigue = 100;
    public float moveFatigueNoLoad = 0.05f;
    public float moveFatigueFullLoad = 0.1f;

    public float hunger = 0;
    public float maxHunger = 100;
    public float moveHungerNoLoad = 0.05f;
    public float moveHungerFullLoad = 0.1f;

    public HealthAspect(Entity entity) {
        super(entity);
        properties = new HashMap<>();
    }
}
