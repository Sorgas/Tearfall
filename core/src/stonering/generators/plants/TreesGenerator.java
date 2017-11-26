package stonering.generators.plants;

import stonering.enums.trees.TreeBlocksTypeEnum;
import stonering.enums.trees.TreeType;
import stonering.enums.materials.MaterialMap;
import stonering.enums.materials.TreeTypeMap;
import stonering.exceptions.MaterialNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.objects.plants.Tree;

import java.util.Random;

/**
 * Created by Alexander on 19.10.2017.
 */
public class TreesGenerator {
    private MaterialMap materialMap;
    private TreeTypeMap treeTypeMap;

    public TreesGenerator(LocalGenContainer container) {
        this.materialMap = container.getMaterialMap();
        this.treeTypeMap = new TreeTypeMap();
    }

    public Tree generateTree(String speciment, int age) throws MaterialNotFoundException {
        TreeType treeType = treeTypeMap.getTreeType(speciment);
        Tree tree = new Tree(speciment, 10, materialMap.getId(treeType.getMaterialName()));
        Random random = new Random();
        int treeCenter = treeType.getCrownRadius();
        int rootsDepth = treeType.getRootDepth();
        int treeWidth = 1 + treeCenter * 2;
        int[][][] treeBlocks = new int[treeWidth][treeWidth][treeType.getHeight() + rootsDepth];
        int branchesStart = treeType.getHeight() / 2 + rootsDepth;
        // stomp
        treeBlocks[treeCenter][treeCenter][rootsDepth] = TreeBlocksTypeEnum.STOMP.getCode();
        // trunk
        for (int i = rootsDepth + 1; i < treeBlocks[0][0].length - 1; i++) {
            treeBlocks[treeCenter][treeCenter][i] = TreeBlocksTypeEnum.TRUNK.getCode();
        }
        // roots
        for (int i = 0; i < rootsDepth; i++) {
            treeBlocks[treeCenter][treeCenter][i] = TreeBlocksTypeEnum.ROOT.getCode();
        }
        // branches
        treeBlocks[treeCenter][treeCenter][treeBlocks[0][0].length - 1] = TreeBlocksTypeEnum.BRANCH.getCode();
        for (int z = branchesStart; z < treeBlocks[0][0].length - 1; z++) {
            for (int x = 0; x < treeWidth; x++) {
                for (int y = 0; y < treeWidth; y++) {
                    if (treeBlocks[x][y][z] == 0) {
                        boolean rollBranch = ((Math.abs(treeCenter - x) <= 1)
                                && (Math.abs(treeCenter - y) <= 1))
                                && (random.nextInt(3) < 2);
                        if(rollBranch) {
                            treeBlocks[x][y][z] = TreeBlocksTypeEnum.BRANCH.getCode();
                        }
                    }
                }
            }
        }
        // crown
        for (int x = 0; x < treeBlocks.length; x++) {
            for (int y = 0; y < treeBlocks[0].length; y++) {
                for (int z = branchesStart; z < treeBlocks[0][0].length; z++) {
                    if (treeBlocks[x][y][z] == 0) {
                        treeBlocks[x][y][z] = TreeBlocksTypeEnum.CROWN.getCode();
                    }
                }
            }
        }
        tree.setBlockTypes(treeBlocks);
        tree.setStompZ(rootsDepth);
        return tree;
    }
}