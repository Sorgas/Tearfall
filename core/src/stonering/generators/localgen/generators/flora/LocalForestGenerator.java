package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.Tree;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Placer class for {@link Tree} plants
 *
 * @author Alexander_Kuzyakov on 25.04.2019.
 */
public class LocalForestGenerator extends LocalFloraGenerator {

    public LocalForestGenerator(LocalGenContainer container) {
        super(container);
    }

    @Override
    protected Set<PlantType> filterPlantsByType() {
        TagLoggersEnum.GENERATION.log("generating trees");
        return PlantMap.getInstance().getTreeTypes().values().stream().filter(PlantType::isTree).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        Collections.shuffle(positions);
        TreeGenerator treeGenerator = new TreeGenerator();
        for (int i = 0; i < amount; i++) {
            if (positions.isEmpty()) return;
            Position position = positions.remove(0);
            Tree tree = treeGenerator.generateTree(specimen, 0);
            tree.setPosition(position);
            container.model.get(PlantContainer.class).place(tree);
        }
    }
}
