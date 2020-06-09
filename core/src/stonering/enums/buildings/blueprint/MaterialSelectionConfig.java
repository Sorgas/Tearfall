package stonering.enums.buildings.blueprint;

import stonering.stage.entity_menu.building.MaterialItemSelectSection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Config, for keeping player selection of materials for building.
 * Materials can be enabled individually with {@link this#map}, or whole type can be enabled with {@link this#types}.
 * When type is selected, items of new material appeared on map will be enabled too.
 * E.g. cut oak and pine wood, select blueprint for building, enable logs(oak and pine),
 * cut birch, select same blueprint and logs of all materials(oak, pine and birch) will be enabled.
 * See {@link MaterialItemSelectSection}
 *
 * @author Alexander on 06.04.2020
 */
public class MaterialSelectionConfig {
    public final Map<String, Set<Integer>> map = new HashMap<>();
    public final Set<String> types = new HashSet<>();
}
