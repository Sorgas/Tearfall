package stonering.entity.unit.aspects.body;

import stonering.enums.unit.health.disease.DiseaseStage;
import stonering.enums.unit.health.disease.DiseaseType;
import stonering.game.model.system.unit.HealthSystem;

/**
 * Whole body disease of some {@link DiseaseType}. Diseases progress from 0 to 1 if not cured.
 * Penalties are stored in {@link DiseaseStage} of type, and applied when progress reaches their ranges by {@link HealthSystem}.
 * 
 * @author Alexander on 10.08.2020.
 */
public class DiseaseState {
    public final DiseaseType type;
    public DiseaseStage stage;
    public float current;

    public DiseaseState(DiseaseType type) {
        this.type = type;
        current = 0;
        updateStage();
    }

    public boolean change(float delta) {
        current += delta;
         if(stage.range.check(current)) return false; // stage did not change
        updateStage();
        return true;
    }

    private void updateStage() {
        stage = type.stages.values().stream()
                .filter(stage -> stage.range.check(current))
                .findFirst()
                .orElse(null);
    }
}
