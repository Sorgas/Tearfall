package stonering.entity.unit.aspects.body;

import java.util.Comparator;

import stonering.entity.unit.Unit;
import stonering.enums.unit.health.disease.DiseaseMap;
import stonering.enums.unit.health.disease.DiseaseStage;
import stonering.enums.unit.health.disease.DiseaseType;

/**
 * Whole body disease of some {@link DiseaseType}. Diseases progress from 0 to 1 if not cured.
 * Penalties are stored in {@link DiseaseStage} of type, and applied when progress reaches their ranges by {@link stonering.game.model.system.unit.CreatureHealthSystem}.
 * 
 * @author Alexander on 10.08.2020.
 */
public class DiseaseState extends HealthEffect {
    public final String name;
    public final DiseaseType type;
    public String stageName;
    public float progress = 0;
    public float nextStage; // 

    public DiseaseState(DiseaseType type) {
        this.type = type;
        name = type.name;
        findNextStage();
    }

    @Override
    public void apply(Unit unit) {

    }

    @Override
    public void unapply(Unit unit) {

    }

    public boolean change(float delta) {
        progress += delta;
        if (progress < nextStage) return false; // stage did not change
        findNextStage();
        return true;
    }

    private void findNextStage() {
        DiseaseMap.get(name).stages.values().stream()
                .filter(stage -> stage.start >= progress) // filter passes stages
                .min(Comparator.comparingDouble(stage -> stage.start)) // next stage
                .ifPresent(stage -> {
                    nextStage = stage.start;
                    stageName = stage.name;
                });
    }
}
