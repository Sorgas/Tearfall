package stonering.entity.unit.aspects.health;


import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.unit.health.HealthParameterEnum;
import stonering.game.model.system.units.CreatureHealthSystem;

import java.util.HashMap;
import java.util.Map;

import static stonering.enums.unit.health.HealthParameterEnum.*;

/**
 * Stores health condition of a unit. See {@link CreatureHealthSystem}
 * TODO add calculation of max values based on creature's attributes.
 *
 * @author Alexander_Kuzyakov
 */
public class HealthAspect extends Aspect {
    public final Map<String, Float> properties;
    public final Map<HealthParameterEnum, HealthParameterState> parameters;

    public HealthAspect(Entity entity) {
        super(entity);
        properties = new HashMap<>();
        parameters = new HashMap<>();
    }
}
