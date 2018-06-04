package stonering.testingChamber;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.plants.TreesGenerator;

public class TreeCutTest {
    private LocalMap localMap;

    public TreeCutTest() {
        localMap = new LocalMap(15,15, 15);
    }

    private void generateSurfece() {
        MaterialMap materialMap = MaterialMap.getInstance();
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getxSize(); y++) {
                localMap.setBlock(x,y,0, BlockTypesEnum.WALL, materialMap.getMaterial("soil").getId());
                localMap.setBlock(x,y,0, BlockTypesEnum.FLOOR, materialMap.getMaterial("soil").getId());
            }
        }
    }

    private void placeTree() throws DescriptionNotFoundException {
        TreesGenerator treesGenerator = new TreesGenerator();
        treesGenerator.generateTree("willow", 0);

    }
}
