package stonering.generators.plants;

import stonering.enums.plants.*;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.MaterialNotFoundException;
import stonering.generators.localgen.LocalGenContainer;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;
import stonering.objects.local_actors.plants.Tree;

import java.util.Random;

/**
 * Created by Alexander on 19.10.2017.
 */
public class TreesGenerator {
    private MaterialMap materialMap;

    public Tree generateTree(String speciment, int age) throws MaterialNotFoundException {
        PlantType plantType = PlantMap.getInstance().getPlantType(speciment);
        TreeType treeType = plantType.getTreeType();
        int material = MaterialMap.getInstance().getId(plantType.getMaterialName());
        Tree tree = new Tree(10, material);
        Random random = new Random();
        int treeCenter = treeType.getCrownRadius();
        int rootsDepth = treeType.getRootDepth();
        int treeWidth = 1 + treeCenter * 2;
        Plant[][][] treeBlocks = new Plant[treeWidth][treeWidth][treeType.getHeight() + rootsDepth];
        int branchesStart = treeType.getHeight() / 2 + rootsDepth;
        // stomp
        treeBlocks[treeCenter][treeCenter][rootsDepth] = createTreePart(material, TreeBlocksTypeEnum.STOMP.getCode(), plantType);
        // trunk
        for (int i = rootsDepth + 1; i < treeBlocks[0][0].length - 1; i++) {

            treeBlocks[treeCenter][treeCenter][i] = createTreePart(material, TreeBlocksTypeEnum.TRUNK.getCode(), plantType);
        }
        // roots
        for (int i = 0; i < rootsDepth; i++) {
            treeBlocks[treeCenter][treeCenter][i] = createTreePart(material, TreeBlocksTypeEnum.ROOT.getCode(), plantType);
        }
        // branches
        treeBlocks[treeCenter][treeCenter][treeBlocks[0][0].length - 1] = createTreePart(material, TreeBlocksTypeEnum.BRANCH.getCode(), plantType);
        for (int z = branchesStart; z < treeBlocks[0][0].length - 1; z++) {
            for (int x = 0; x < treeWidth; x++) {
                for (int y = 0; y < treeWidth; y++) {
                    if (treeBlocks[x][y][z] == null) {
                        boolean rollBranch = ((Math.abs(treeCenter - x) <= 1)
                                && (Math.abs(treeCenter - y) <= 1))
                                && (random.nextInt(3) < 2);
                        if(rollBranch) {
                            treeBlocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.BRANCH.getCode(), plantType);
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
                        treeBlocks[x][y][z] = createTreePart(material, TreeBlocksTypeEnum.CROWN.getCode(), plantType);
                    }
                }
            }
        }
        tree.setBlocks(treeBlocks);
        tree.setStompZ(rootsDepth);
        return tree;
    }

    private Plant createTreePart(int material, int blockType, PlantType plantType) {
        Plant plant = new Plant(0);
        plant.setType(plantType);
        PlantBlock block = new PlantBlock(material, blockType);
        block.setAtlasY(plantType.getAtlasY());
        block.setAtlasX(TreeTileMapping.getType(blockType).getAtlasX());
        plant.setBlock(block);
        return plant;
    }
}