package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.Disease;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.game.model.system.EntitySystem;

/**
 * System for applying and updating diseases, applying wounds, and healing. 
 * 
 * @author Alexander on 13.08.2020.
 */
public class CreatureDiseaseSystem extends EntitySystem<Unit> {

    @Override
    public void update(Unit unit) {
        BodyAspect aspect = unit.get(BodyAspect.class);
        
    }

    public void addNewDisease(Unit unit, Disease disease) {
        BodyAspect aspect = unit.get(BodyAspect.class);
        aspect.diseases.putIfAbsent(disease.name, disease);
        unit.get(HealthAspect.class).functions
    }
}
