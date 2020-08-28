package stonering.enums.unit.health.disease;

import java.util.HashMap;
import java.util.Map;

import stonering.entity.unit.aspects.body.HealthEffect;
import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.util.math.ValueRange;

/**
 * Stage of {@link DiseaseType} with its own effects and range of disease progression.
 *
 * @author Alexander on 8/14/2020
 */
public class DiseaseStage extends HealthEffect {
    public final String name;
    public final ValueRange range;

    public DiseaseStage(RawDiseaseStage raw) {
        name = raw.name;
        range = new ValueRange(raw.start, null);
    }
}
