package stonering.enums.unit.health.disease;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Alexander on 8/14/2020
 */
public class DiseaseType {
    public String name;
    public Map<String, DiseaseStage> stages; // stage name to stage

    public DiseaseType(RawDiseaseType raw) {
        name = raw.name;
        stages = new HashMap<>();
    }
}
