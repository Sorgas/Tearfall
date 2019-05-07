package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

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
        TagLoggersEnum.GENERATION.log("generating trees");
        return PlantMap.getInstance().getTreeTypes().values().stream().filter(PlantType::isTree).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        try {
            amount /= 20; // amount is lowered for trees.
            Collections.shuffle(positions);
            TreeGenerator treeGenerator = new TreeGenerator();
            int maxAge = PlantMap.getInstance().getTreeType(specimen).getMaxAge();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                Tree tree = treeGenerator.generateTree(specimen, random.nextInt(maxAge));
                if (!checkTreePlacing(tree, position.x, position.y, position.z)) continue;
                tree.setPosition(position);
                container.model.get(PlantContainer.class).place(tree);
            }
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * Checks that desired area for tree is free.
     */
    private boolean checkTreePlacing(Tree tree, int cx, int cy, int cz) {
        PlantBlock[][][] treeParts = tree.getBlocks();
        int treeCenterZ = tree.getCurrentStage().treeForm.get(2);
        int treeRadius = tree.getCurrentStage().treeForm.get(0);
        for (int x = 0; x < treeParts.length; x++) {
            for (int y = 0; y < treeParts[x].length; y++) {
                for (int z = 0; z < treeParts[x][y].length; z++) {
                    int mapX = cx + x - treeRadius;
                    int mapY = cy + y - treeRadius;
                    int mapZ = cz + z - treeCenterZ;
                    if (treeParts[x][y][z] == null) continue;
                    if (!localMap.inMap(mapX, mapY, mapZ)) continue;
                    //TODO add blocks validation
                    if (plantContainer.getPlantBlocks().containsKey(cachePosition.set(mapX, mapY, mapZ))) return false;
                }
            }
        }
        return hasTreesNear(tree.getPosition(), 2);
    }

    private boolean hasTreesNear(Position position, int radius) {
        for (int x = position.x - radius; x < position.x + radius; x++) {
            for (int y = position.y - radius; y < position.y + radius; y++) {
                for (int z = position.z - 1; z < position.z + 3; z++) {
                    if(plantContainer.getPlantBlocks().get(cachePosition.set(x,y,z)) != null) return false;
                }
            }
        }
        return true;
    }
}
