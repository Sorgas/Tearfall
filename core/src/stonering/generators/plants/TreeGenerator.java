package stonering.generators.plants;

import stonering.enums.materials.MaterialMap;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.enums.plants.TreeBlocksTypeEnum;
import stonering.enums.plants.TreeTileMapping;
import stonering.enums.plants.TreeType;
import stonering.util.geometry.Position;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;

import java.util.Random;

/**
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class TreeGenerator {

    //TODO refactor
    public Tree generateTree(String specimen, int age) throws IllegalArgumentException {
        PlantType plantType = PlantMap.getInstance().getPlantType(specimen);
        Tree tree = new Tree(null, age);
        tree.setType(plantType);
        tree.setBlocks(createTreeBlocks(tree, ));
        return tree;
    }

    /**
     * Changes tree structure.
     *
     * @param tree
     */
    public void applyTreeGrowth(Tree tree) {
        PlantType.PlantLifeStage lifeStage = tree.getCurrentStage();
    }

    /**
     * Counts {@param plantType} life stage corresponding to {@param age} (months).
     */
    private PlantType.PlantLifeStage getStageByAge(PlantType plantType, int age) throws IllegalArgumentException {
        for (PlantType.PlantLifeStage lifeStage : plantType.getLifeStages()) {
            age -= lifeStage.getStageLength();
            if (age <= 0) return lifeStage;
        }
        throw new IllegalArgumentException("Age " + age + " is invalid for treeType " + plantType.getName());
    }

    /**
     * Creates blocks for tree. Parameters are taken from current life stage.
     * @param tree
     * @return
     */
    private PlantBlock[][][] createTreeBlocks(Tree tree) {
        Random random = new Random();
        TreeType treeType = tree.getCurrentStage().getTreeType();
        int material = MaterialMap.getInstance().getId(tree.getType().getLifeStages().get(0).getMaterialName());
        int treeCenter = treeType.getCrownRadius();
        int rootsDepth = treeType.getRootDepth();
        int treeWidth = 1 + treeCenter * 2;
        PlantBlock[][][] treeBlocks = new PlantBlock[treeWidth][treeWidth][treeType.getHeight() + rootsDepth];
        int branchesStart = treeType.getHeight() / 2 + rootsDepth;
        // stomp
        treeBlocks[treeCenter][treeCenter][rootsDepth] = createTreePart(material, TreeBlocksTypeEnum.STOMP, tree, treeCenter, treeCenter, rootsDepth);
        // trunk
        for (int i = rootsDepth + 1; i < treeBlocks[0][0].length - 1; i++) {
            treeBlocks[treeCenter][treeCenter][i] = createTreePart(material, TreeBlocksTypeEnum.TRUNK, tree, treeCenter, treeCenter, i);
        }
        // roots
        for (int i = 0; i < rootsDepth; i++) {
            treeBlocks[treeCenter][treeCenter][i] = createTreePart(material, TreeBlocksTypeEnum.ROOT, tree, treeCenter, treeCenter, i);
        }
        // branches
        treeBlocks[treeCenter][treeCenter][treeBlocks[0][0].length - 1] = createTreePart(material, TreeBlocksTypeEnum.BRANCH, tree, treeCenter, treeCenter, treeBlocks[0][0].length - 1);
        for (int z = branchesStart; z < treeBlocks[0][0].length - 1; z++) {
            for (int x = 0; x < treeWidth; x++) {
                for (int y = 0; y < treeWidth; y++) {
                    if (treeBlocks[x][y][z] == null) {
                        boolean rollBranch = ((Math.abs(treeCenter - x) <= 1)
                                && (Math.abs(treeCenter - y) <= 1))
                                && (random.nextInt(3) < 2);
                        if (rollBranch) {
                            treeBlocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.BRANCH, tree, x, y, z);
                        }
                    }
                }
            }
        }
        // crown
        for (int x = 0; x < treeBlocks.length; x++) {
            for (int y = 0; y < treeBlocks[0].length; y++) {
                for (int z = branchesStart; z < treeBlocks[0][0].length; z++) {
                    if (treeBlocks[x][y][z] == null) {
                        treeBlocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.CROWN, tree, x, y, z);
                    }
                }
            }
        }
        return treeBlocks;
    }

    private PlantBlock createTreePart(int material, TreeBlocksTypeEnum blockType, Tree tree, int x, int y, int z) {
        PlantBlock block = new PlantBlock(material, blockType.getCode());
        block.setPosition(new Position(x, y, z));
        block.setAtlasXY(new int[]{
                TreeTileMapping.getType(blockType.getCode()).getAtlasX(),
                tree.getCurrentStage().getAtlasXY()[1]});
        block.setPlant(tree);
        return block;
    }
}