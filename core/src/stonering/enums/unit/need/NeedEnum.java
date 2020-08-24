package stonering.enums.unit.need;

import java.util.HashMap;
import java.util.Map;

import stonering.enums.unit.need.hunger.FoodNeed;
import stonering.enums.unit.need.rest.RestNeed;
import stonering.enums.unit.need.thirst.WaterNeed;
import stonering.enums.unit.need.wear.WearNeed;

/**
 * Creatures needs ore enumerated here. Each unit has counters of it's needs.
 * If need counter reaches 0, special disease is applied.
 * TODO move need names to needs
 *
 * @author Alexander on 22.08.2019.
 */
public enum NeedEnum {
    WEAR("wear", new WearNeed(null, "no_wear")),
    REST("rest", new RestNeed("fatigue", "tiredness")),
    FOOD("food", new FoodNeed("starvation", "hunger")),
    WATER("water", new WaterNeed("dehydration", "thirst")),
//    WARMTH("warmth", null); // TODO
    ;

    public static final Map<String, NeedEnum> map = new HashMap<>();

    static {
        for (NeedEnum value : NeedEnum.values()) {
            map.put(value.NAME, value);
            value.NEED.need = value; // link after creation, because cant access enum constant from its own constructor
        }
    }

    public final String NAME;
    public final Need NEED;

    NeedEnum(String name, Need need) {
        NAME = name;
        NEED = need;
    }
}
