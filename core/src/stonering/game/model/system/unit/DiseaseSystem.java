package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.DiseaseState;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.game.model.system.EntitySystem;

/**
 * System for applying and updating diseases, applying wounds, and healing. 
 * Diseases added by needs are rolled up if need is not satisfied, and rolled down otherwise.
 *
 * @author Alexander on 13.08.2020.
 */
public class DiseaseSystem extends EntitySystem<Unit> {

    @Override
    public void update(Unit unit) {
        BodyAspect aspect = unit.get(BodyAspect.class);
        NeedAspect needAspect = unit.get(NeedAspect.class);
        if(aspect == null) return;
        for (DiseaseState state : aspect.diseases.values()) {
            float delta = 0.01f;
            if(state.type.relatedNeed != null) {
                NeedState needState = needAspect.needs.get(state.type.relatedNeed);
                if(needState != null && needState.current() < needState.max) {
                    delta = - 0.01f;
                    //TODO use disease resistance
                }
            }
            state.change(delta);
            
            // check stage
        }
    }

    public void addNewDisease(Unit unit, DiseaseState disease) {
        BodyAspect aspect = unit.get(BodyAspect.class);
        if(!aspect.diseases.containsKey(disease.name)) {
            aspect.diseases.putIfAbsent(disease.name, disease);
            disease.apply(unit);
        }
    }
}
