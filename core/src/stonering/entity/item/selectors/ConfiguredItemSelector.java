package stonering.entity.item.selectors;

import stonering.entity.item.Item;

import java.util.Map;
import java.util.Set;

/**
 * Items selector which can select any combination of item types and materials.
 *
 * @author Alexander on 05.04.2020
 */
public class ConfiguredItemSelector extends ItemSelector {
    public final Map<String, Set<Integer>> map; // types to materials

    public ConfiguredItemSelector(Map<String, Set<Integer>> map) {
        this.map = map;
    }

    @Override
    public boolean checkItem(Item item) {
        return map.containsKey(item.type.name) && map.get(item.type.name).contains(item.material);
    }
}
