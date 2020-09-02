package stonering.enums.unit.health.disease;

import stonering.enums.unit.health.HealthEffect;
import stonering.util.geometry.ValueRange;

/**
 * Stage of {@link DiseaseType} with its own effects and range of disease progression.
 *
 * @author Alexander on 8/14/2020
 */
public class DiseaseStage extends HealthEffect {
    public final ValueRange range;

    public DiseaseStage(RawDiseaseStage raw) {
        super(raw.name);
        range = new ValueRange(raw.start, null);
    }
}
