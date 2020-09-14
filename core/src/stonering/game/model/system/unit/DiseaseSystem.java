package stonering.game.model.system.unit;

import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.body.BodyAspect;
import stonering.entity.unit.aspects.body.DiseaseState;
import stonering.entity.unit.aspects.health.HealthAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.unit.health.disease.DiseaseStage;
import stonering.enums.unit.health.disease.DiseaseType;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;

/**
 * System for applying and updating diseases, applying wounds, and healing.
 * Diseases added by needs are rolled up if need is not satisfied, and rolled down otherwise.
 * <p>
 * TODO use disease resistance (delta = delta - resistance);
 *
 * @author Alexander on 13.08.2020.
 */
public class DiseaseSystem extends EntitySystem<Unit> {
    private final float DISEASE_DELTA = 0.01f;

    @Override
    public void update(Unit unit) {
        BodyAspect body = unit.get(BodyAspect.class);
        NeedAspect needAspect = unit.get(NeedAspect.class);
        HealthSystem healthSystem = GameMvc.model().get(UnitContainer.class).healthSystem;
        if (body == null) return;
        for (DiseaseState state : body.diseases.values()) {
            float delta = DISEASE_DELTA;
            if (state.type.relatedNeed != null) {
                NeedState needState = needAspect.needs.get(state.type.relatedNeed);
                if (needState != null && needState.current() < needState.max) delta = -delta; // disease diminishes if need is satisfied
            }
            DiseaseStage prevStage = state.stage;
            if (!state.change(delta)) continue; // stage did not changed
            healthSystem.unapplyEffect(prevStage, unit); // unapply previous effect
            if(state.stage != null) { // stage changed to another stage
                healthSystem.applyEffect(state.stage, unit);
            } else if (state.current <= 0) {
                body.diseases.remove(state.type.name);
            } else if (state.current >= state.stage.range.max) {
                System.out.println("died of " + state.stage.name);
            }
        }
    }

    public void addNewDisease(Unit unit, DiseaseType diseaseType) {
        BodyAspect aspect = unit.get(BodyAspect.class);
        aspect.diseases.computeIfAbsent(diseaseType.name, diseaseName -> {
            DiseaseState state = new DiseaseState(diseaseType);
            GameMvc.model().get(UnitContainer.class).healthSystem.applyEffect(state.stage, unit);
            return state;
        });
    }
}
