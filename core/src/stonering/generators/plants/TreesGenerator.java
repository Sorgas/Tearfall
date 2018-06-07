package stonering.generators.plants;

import stonering.enums.plants.*;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.global.utils.Position;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.plants.Tree;

import java.util.Random;

/**
 * Created by Alexander on 19.10.2017.
 */
public class TreesGenerator {

    public Tree generateTree(String speciment, int age) throws DescriptionNotFoundException {
        Plant plant = new Plant(0);
        PlantType plantType = PlantMap.getInstance().getPlantType(speciment);
        TreeType treeType = plantType.getTreeType();
        int material = MaterialMap.getInstance().getId(plantType.getMaterialName());
        Tree tree = new Tree(10);
        tree.setType(plantType);
        Random random = new Random();
        int treeCenter = treeType.getCrownRadius();
        int rootsDepth = treeType.getRootDepth();
        int treeWidth = 1 + treeCenter * 2;
        PlantBlock[][][] treeBlocks = new PlantBlock[treeWidth][treeWidth][treeType.getHeight() + rootsDepth];
        int branchesStart = treeType.getHeight() / 2 + rootsDepth;
        // stomp
        treeBlocks[treeCenter][treeCenter][rootsDepth] = createTreePart(material, TreeBlocksTypeEnum.STOMP.getCode(), plantType, tree, treeCenter, treeCenter, rootsDepth);
        // trunk
        for (int i = rootsDepth + 1; i < treeBlocks[0][0].length - 1; i++) {
            treeBlocks[treeCenter][treeCenter][i] = createTreePart(material, TreeBlocksTypeEnum.TRUNK.getCode(), plantType, tree, treeCenter, treeCenter, i);
        }
        // roots
        for (int i = 0; i < rootsDepth; i++) {
            treeBlocks[treeCenter][treeCenter][i] = createTreePart(material, TreeBlocksTypeEnum.ROOT.getCode(), plantType, tree, treeCenter, treeCenter, i);
        }
        // branches
        treeBlocks[treeCenter][treeCenter][treeBlocks[0][0].length - 1] = createTreePart(material, TreeBlocksTypeEnum.BRANCH.getCode(), plantType, tree, treeCenter, treeCenter, treeBlocks[0][0].length - 1);
        for (int z = branchesStart; z < treeBlocks[0][0].length - 1; z++) {
            for (int x = 0; x < treeWidth; x++) {
                for (int y = 0; y < treeWidth; y++) {
                    if (treeBlocks[x][y][z] == null) {
                        boolean rollBranch = ((Math.abs(treeCenter - x) <= 1)
                                && (Math.abs(treeCenter - y) <= 1))
                                && (random.nextInt(3) < 2);
                        if (rollBranch) {
                            treeBlocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.BRANCH.getCode(), plantType, tree, x, y, z);
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
                        treeBlocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.CROWN.getCode(), plantType, tree, x, y, z);
                    }
                }
            }
        }

        tree.setBlocks(treeBlocks);
        return tree;
    }

    private PlantBlock createTreePart(int material, int blockType, PlantType plantType, Tree tree, int x, int y, int z) {
        PlantBlock block = new PlantBlock(material, blockType);
        block.setPosition(new Position(x, y, z));
        block.setAtlasY(plantType.getAtlasY());
        block.setAtlasX(TreeTileMapping.getType(blockType).getAtlasX());
        block.setPlant(tree);
        return block;
    }
}