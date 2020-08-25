package stonering.enums.unit.need;

import java.util.HashMap;
import java.util.Map;

import stonering.enums.unit.health.disease.DiseaseMap;
import stonering.enums.unit.need.hunger.FoodNeed;
import stonering.enums.unit.need.rest.RestNeed;
import stonering.enums.unit.need.thirst.WaterNeed;
import stonering.enums.unit.need.wear.WearNeed;

/**
 * Creatures needs ore enumerated here. Each unit has counters of it's needs.
 * If need counter reaches 0, special disease is applied.
 *
 * @author Alexander on 22.08.2019.
 */
public enum NeedEnum {
    WEAR("wear", new WearNeed("no_wear")),
    REST("rest", "fatigue", new RestNeed("tiredness")),
    FOOD("food", "starvation", new FoodNeed("hunger")),
    WATER("water", "dehydration", new WaterNeed("thirst")),
//    WARMTH("warmth", null); // TODO
    ;

    public static final Map<String, NeedEnum> map = new HashMap<>();

    static {
        for (NeedEnum value : NeedEnum.values()) {
            map.put(value.NAME, value);
            value.NEED.need = value; // link after creation, because cant access enum constant from its own constructor
            value.NEED.disease = DiseaseMap.get(value.DISEASE);
        }
    }

    public final String NAME;
    public final Need NEED;
    public final String DISEASE;

    NeedEnum(String name, String disease, Need need) {
        NAME = name;
        DISEASE = disease;
        NEED = need;
    }

    NeedEnum(String name, Need need) {
        this(name, null, need);
    }
}
