package stonering.enums.unit.health.disease;

import java.util.HashMap;
import java.util.Map;

import stonering.enums.unit.health.HealthFunctionEnum;

/**
 * @author Alexander on 8/14/2020
 */
public class DiseaseStage {
    public final String name;
    public final float start;
    public final Map<HealthFunctionEnum, Float> effectsMap;

    public DiseaseStage(RawDiseaseStage raw) {
        name = raw.name;
        start = raw.start;
        effectsMap = new HashMap<>();
    }
}
