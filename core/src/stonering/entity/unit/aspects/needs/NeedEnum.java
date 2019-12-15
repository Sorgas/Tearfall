package stonering.entity.unit.aspects.needs;

import stonering.util.global.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Creatures needs ore enumerated here.
 * TODO move need names to needs
 *
 * @author Alexander on 22.08.2019.
 */
public enum NeedEnum {
    WEAR("wear", new WearNeed()),
    REST("rest", new RestNeed()),
    FOOD("food", new FoodNeed()),
    WATER("water", new WaterNeed());

    public static final Map<String, NeedEnum> map = new HashMap<>();
    static {
        for (NeedEnum value : NeedEnum.values()) {
            map.put(value.NAME, value);
        }
    }
    public final String NAME;
    public final Need NEED;

    NeedEnum(String name, Need need) {
        NAME = name;
        this.NEED = need;
    }

    public static NeedEnum get(String needName) {
        if(!map.containsKey(needName)) Logger.UNITS.logWarn("Getting invalid need " + needName);
        return map.get(needName);
    }
}
