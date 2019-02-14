package stonering.generators.plants;

import stonering.entity.local.plants.aspects.PlantGrowthAspect;
import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.TreeBlocksTypeEnum;
import stonering.enums.plants.TreeTileMapping;
import stonering.enums.plants.TreeType;
import stonering.util.geometry.Position;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;

import java.util.Random;

/**
 * Generates trees and used for tree growth.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class TreeGenerator {

    public Tree generateTree(String specimen, int age) throws IllegalArgumentException {
        Tree tree = new Tree(null, age);
        tree.setType(PlantMap.getInstance().getPlantType(specimen));
        tree.setBlocks(createTreeBlocks(tree));
        tree.getAspects().put(PlantGrowthAspect.NAME, new PlantGrowthAspect(tree));
        return tree;
    }

    /**
     * Changes tree structure.
     *
     * @param tree
     */
    public void applyTreeGrowth(Tree tree) {
        tree.setBlocks(createTreeBlocks(tree));
    }

    /**
     * Creates tree blocks array
     */
    private PlantBlock[][][] createTreeBlocks(Tree tree) {
        TreeType treeType = tree.getCurrentStage().getTreeType();
        int material = MaterialMap.getInstance().getId(tree.getCurrentStage().getMaterialName());
        Random random = new Random();
        int center = treeType.getCrownRadius();
        int rootsDepth = treeType.getRootDepth();
        int treeWidth = 1 + center * 2;
        int branchesStart = treeType.getHeight() / 2 + rootsDepth;
        PlantBlock[][][] blocks = new PlantBlock[treeWidth][treeWidth][treeType.getHeight() + rootsDepth];

        // stomp. single block
        blocks[center][center][rootsDepth] = createTreePart(material, TreeBlocksTypeEnum.STOMP, tree);
        // trunk. from stomp to top
        for (int i = rootsDepth + 1; i < blocks[0][0].length - 1; i++) {
            blocks[center][center][i] = createTreePart(material, TreeBlocksTypeEnum.TRUNK, tree);
        }
        // roots
        for (int i = 0; i < rootsDepth; i++) {
            blocks[center][center][i] = createTreePart(material, TreeBlocksTypeEnum.ROOT, tree);
        }
        // branches
        if(blocks[center][center][blocks[0][0].length - 1] == null)blocks[center][center][blocks[0][0].length - 1] = createTreePart(material, TreeBlocksTypeEnum.BRANCH, tree);
        for (int z = branchesStart; z < blocks[0][0].length - 1; z++) {
            for (int x = center - 1; x <= center + 1; x++) {
                for (int y = center - 1; y <= center + 1; y++) {
                    if (blocks[x][y][z] == null && random.nextInt(3) < 2) {
                        blocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.BRANCH, tree);
                    }
                }
            }
        }
        // crown
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = branchesStart; z < blocks[0][0].length; z++) {
                    if (blocks[x][y][z] == null) {
                        blocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.CROWN, tree);
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

    private PlantBlock createTreePart(int material, TreeBlocksTypeEnum blockType, Tree tree) {
        PlantBlock block = new PlantBlock(material, blockType.getCode());
        block.setAtlasXY(new int[]{
                TreeTileMapping.getType(blockType.getCode()).getAtlasX(),
                tree.getCurrentStage().getAtlasXY()[1]});
        block.setPlant(tree);
        return block;
    }
}