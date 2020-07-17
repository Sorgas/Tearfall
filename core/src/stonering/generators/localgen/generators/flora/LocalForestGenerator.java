package stonering.generators.localgen.generators.flora;

import stonering.entity.plant.PlantBlock;
import stonering.entity.plant.Tree;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.plants.PlantLifeStage;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Placer class for {@link Tree} plants
 *
 * @author Alexander_Kuzyakov on 25.04.2019.
 */
public class LocalForestGenerator extends LocalFloraGenerator {
    private Random random;

    public LocalForestGenerator(LocalGenContainer container) {
        super(container);
        random = new Random();
    }

    @Override
    protected Set<PlantType> filterPlantsByType() {
        Logger.GENERATION.log("generating trees");
        return PlantTypeMap.instance().treeTypes.values().stream().filter(type -> type.isTree).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        int counter = 0;
        amount /= 20; // amount is lowered for trees.
        Collections.shuffle(positions);
        TreeGenerator treeGenerator = new TreeGenerator();
        int maxAge = PlantTypeMap.getTreeType(specimen).getMaxAge();
        for (int i = 0; i < amount; i++) {
            if (positions.isEmpty()) return;
            Position position = positions.remove(0);
            Tree tree = treeGenerator.generateTree(specimen, random.nextInt(maxAge));
            if (!checkTreePlacing(tree, position.x, position.y, position.z)) continue;
            container.model.get(PlantContainer.class).add(tree, position);
            counter++;
        }
        Logger.GENERATION.logDebug(counter + " trees placed.");
    }

    /**
     * Checks that desired area for tree is free.
     */
    private boolean checkTreePlacing(Tree tree, int cx, int cy, int cz) {
        PlantBlock[][][] treeParts = tree.getBlocks();
        PlantLifeStage stage = tree.type.lifeStages.get(tree.get(PlantGrowthAspect.class).stageIndex);
        int treeCenterZ = stage.treeForm.get(2);
        int treeRadius = stage.treeForm.get(0);
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    int mapX = cx + x - treeRadius;
                    int mapY = cy + y - treeRadius;
                    int mapZ = cz + z - treeCenterZ;
                    if (treeParts[x][y][z] == null) continue;
                    if (!localMap.inMap(mapX, mapY, mapZ)) continue;
                    //TODO add blocks validation
                    if (plantContainer.isPlantBlockExists(cachePosition.set(mapX, mapY, mapZ))) return false;
                }
            }
        }
        return !hasTreesNear(new Position(cx, cy, cz), 2);
    }

    /**
     * Checks that in radius around position no plant blocks is present (There is only tree blocks on tree generation stage).
     */
    private boolean hasTreesNear(Position position, int radius) {
        for (int x = position.x - radius; x < position.x + radius; x++) {
            for (int y = position.y - radius; y < position.y + radius; y++) {
                for (int z = position.z - 1; z < position.z + 3; z++) {
                    if (plantContainer.isPlantBlockExists(cachePosition.set(x, y, z))) return true;
                }
            }
        }
        return false;
    }
}
