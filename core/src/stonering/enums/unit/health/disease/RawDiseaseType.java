package stonering.enums.unit.health.disease;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander on 8/14/2020
 */
public class RawDiseaseType {
    public String name;
    public List<RawDiseaseStage> stages = new ArrayList<>();
    public String medicineType;
    public String relatedNeed;
}
