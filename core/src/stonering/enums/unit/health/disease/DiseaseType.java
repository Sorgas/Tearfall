package stonering.enums.unit.health.disease;

import java.util.HashMap;
import java.util.Map;

import stonering.enums.unit.need.NeedEnum;

/**
 * Type for disease. 
 * {@link #relatedNeed} specifies
 * 
 * @author Alexander on 8/14/2020
 */
public class DiseaseType {
    public String name;
    public Map<String, DiseaseStage> stages; // stage name to stage
    public NeedEnum relatedNeed; // 
    
    public DiseaseType(RawDiseaseType raw) {
        name = raw.name;
        stages = new HashMap<>();
        relatedNeed = NeedEnum.map.get(raw.relatedNeed);
    }
}
