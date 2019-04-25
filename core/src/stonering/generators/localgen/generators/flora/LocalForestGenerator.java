package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.Tree;
import stonering.enums.plants.PlantMap;
import stonering.generators.localgen.LocalGenContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Placer class for {@link Tree} plants
 */
public class LocalForestGenerator extends LocalFloraGenerator {
    private Map<String, Float> weightedTreeTypes;

    public LocalForestGenerator(LocalGenContainer container) {
        super(container);
    }

    @Override
    public void execute() {
        weightedTreeTypes = new HashMap<>();
        filterTrees();
        normalizeWeights(weightedTreeTypes);
    }

    private void filterTrees() {
        PlantMap.getInstance().getAllTypes().stream().filter(type -> type.isTree()).forEach(type -> {
            weightedTreeTypes.put(type.name, getSpreadModifier(type.name));
        });
    }
}
