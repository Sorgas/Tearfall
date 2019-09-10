package stonering.entity.unit.aspects.equipment;

import java.util.HashMap;
import java.util.Map;

/**
 * Layers of equipment. Each slot can hold one item of each layer.
 *
 * @author Alexander_Kuzyakov on 09.09.2019.
 */
public enum WearLayers {
    UNDER(0, "under"),
    MEDIUM(1, "medium"),
    UPPER(2, "upper"),
    ARMOR(3, "armor"),
    ABOVE(4, "above");

    public final int index;
    public final String name;

    public static Map<String, WearLayers> map = new HashMap();

    static {
        for (WearLayers value : WearLayers.values()) {
            map.put(value.name, value);
        }
    }

    WearLayers(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static WearLayers getByName(String name) {
        return map.get(name);
    }
}
