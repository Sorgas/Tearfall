package stonering.generators.plants;

import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantBlocksTypeEnum;
import stonering.enums.plants.PlantType;
import stonering.enums.plants.TreeTileMapping;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.util.geometry.Position;
import stonering.entity.plant.PlantBlock;
import stonering.entity.plant.Tree;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Generates trees and is used for tree growth.
 * Does not set tree's positions.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class TreeGenerator {

    public Tree generateTree(String specimen, int age) throws DescriptionNotFoundException {
        PlantType type = PlantTypeMap.getInstance().getTreeType(specimen);
        Tree tree = new Tree(type);
        //TODO set age
        tree.addAspect(new PlantGrowthAspect(tree, age));
        tree.setBlocks(createTreeBlocks(tree));
        return tree;
    }

    /**
     * Changes tree structure.
     */
    public void applyTreeGrowth(Tree tree) {
        tree.setBlocks(createTreeBlocks(tree));
    }

    /**
     * Creates tree blocks array
     */
    private PlantBlock[][][] createTreeBlocks(Tree tree) {
        List<Integer> treeForm = tree.type.lifeStages.get(tree.getAspect(PlantGrowthAspect.class).currentStage).treeForm;
        int material = MaterialMap.instance().getId(tree.type.materialName);
        Random random = new Random();
        int center = treeForm.get(0);
        int rootsDepth = treeForm.get(2);
        int treeWidth = 1 + center * 2;
        int branchesStart = treeForm.get(1) / 2 + rootsDepth;
        PlantBlock[][][] blocks = new PlantBlock[treeWidth][treeWidth][treeForm.get(1) + rootsDepth];

        // stomp. single block
        blocks[center][center][rootsDepth] = createTreePart(material, PlantBlocksTypeEnum.STOMP, tree);
        // trunk. from stomp to top
        for (int i = rootsDepth + 1; i < blocks[0][0].length - 1; i++) {
            blocks[center][center][i] = createTreePart(material, PlantBlocksTypeEnum.TRUNK, tree);
        }
        // roots
        for (int i = 0; i < rootsDepth; i++) {
            blocks[center][center][i] = createTreePart(material, PlantBlocksTypeEnum.ROOT, tree);
        }
        // branches
        if(blocks[center][center][blocks[0][0].length - 1] == null)blocks[center][center][blocks[0][0].length - 1] = createTreePart(material, PlantBlocksTypeEnum.BRANCH, tree);
        for (int z = branchesStart; z < blocks[0][0].length - 1; z++) {
            for (int x = center - 1; x <= center + 1; x++) {
                for (int y = center - 1; y <= center + 1; y++) {
                    if (blocks[x][y][z] == null && random.nextInt(3) < 2) {
                        blocks[x][y][z] = createTreePart(material, PlantBlocksTypeEnum.BRANCH, tree);
                    }
                }
            }
        }
        // crown
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = branchesStart; z < blocks[0][0].length; z++) {
                    if (blocks[x][y][z] == null) {
                        blocks[x][y][z] = createTreePart(material, PlantBlocksTypeEnum.CROWN, tree);
                    }
                }
            }
        }
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = branchesStart; z < blocks[0][0].length; z++) {
                    if (blocks[x][y][z] != null) blocks[x][y][z].setPosition(new Position(x,y,z));
                }
            }
        }
        return blocks;
    }

    private PlantBlock createTreePart(int material, PlantBlocksTypeEnum blockType, Tree tree) {
        PlantBlock block = new PlantBlock(material, blockType.getCode());
        int[] atlasXY = Arrays.copyOf(tree.type.atlasXY, 2);
        atlasXY[0] += TreeTileMapping.getType(blockType.getCode()).getAtlasX();
        block.setAtlasXY(atlasXY);
        block.setPlant(tree);
        return block;
    }
}