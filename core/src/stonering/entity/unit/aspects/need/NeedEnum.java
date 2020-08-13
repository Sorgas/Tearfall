package stonering.entity.unit.aspects.need;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import stonering.entity.unit.aspects.body.Disease;
import stonering.util.logging.Logger;

/**
 * Creatures needs ore enumerated here. Each unit has counters of it's needs.
 * If need counter reaches 0, special disease is applied.
 * TODO move need names to needs
 *
 * @author Alexander on 22.08.2019.
 */
public enum NeedEnum {
    WEAR("wear", new WearNeed()),
    REST("rest", new RestNeed()),
    FOOD("food", new FoodNeed()),
    WATER("water", new WaterNeed()),
    WARMTH("warmth", null); // TODO 

    public static final Map<String, NeedEnum> map = new HashMap<>();

    static {
        for (NeedEnum value : NeedEnum.values()) {
            map.put(value.NAME, value);
        }
    }

    public final String NAME;
    public final Need NEED;
    public final Supplier<Disease> DISEASE_SUPPLIER;

    NeedEnum(String name, Need need, Supplier<Disease> supplier) {
        NAME = name;
        NEED = need;
        DISEASE_SUPPLIER = supplier;
    }

    NeedEnum(String name, Need need) {
        this(name, need, () -> null);
    }

    public static NeedEnum get(String needName) {
        if (!map.containsKey(needName)) Logger.UNITS.logWarn("Getting invalid need " + needName);
        return map.get(needName);
    }
}
