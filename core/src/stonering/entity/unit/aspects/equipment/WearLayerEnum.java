package stonering.entity.unit.aspects.equipment;

import java.util.HashMap;
import java.util.Map;

/**
 * Layers of equipment. Each slot can hold one item of each layer.
 *
 * @author Alexander_Kuzyakov on 09.09.2019.
 */
public enum WearLayerEnum {
    UNDER(0, "under"),
    MEDIUM(1, "medium"),
    UPPER(2, "upper"),
    ARMOR(3, "armor"),
    ABOVE(4, "above");

    public final int INDEX;
    public final String NAME;

    public static Map<String, WearLayerEnum> map = new HashMap();

    static {
        for (WearLayerEnum value : WearLayerEnum.values()) {
            map.put(value.NAME, value);
        }
    }

    WearLayerEnum(int index, String name) {
        this.INDEX = index;
        this.NAME = name;
    }

    public static WearLayerEnum getByName(String name) {
        return map.get(name);
    }
}
