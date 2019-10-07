package stonering.entity.unit.aspects.needs;

import java.util.HashMap;
import java.util.Map;

/**
 * Creatures needs ore enumerated here.
 *
 * @author Alexander on 22.08.2019.
 */
public enum NeedEnum {
    WEAR("wear", new WearNeed()),
    REST("rest", new RestNeed());

    public static final Map<String, NeedEnum> map = new HashMap<>();
    static {
        for (NeedEnum value : NeedEnum.values()) {
            map.put(value.NAME, value);
        }
    }
    public final String NAME;
    public final Need need;

    NeedEnum(String name, Need need) {
        NAME = name;
        this.need = need;
    }
}
