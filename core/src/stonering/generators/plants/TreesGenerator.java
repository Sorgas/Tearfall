package stonering.generators.plants;

import stonering.enums.TreeBlocksTypeEnum;
import stonering.enums.TreeType;
import stonering.enums.materials.MaterialMap;
import stonering.enums.materials.TreeTypeMap;
import stonering.exceptions.MaterialNotFoundException;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.global.FileLoader;
import stonering.objects.plants.Tree;

import java.util.Random;

/**
 * Created by Alexander on 19.10.2017.
 */
public class TreesGenerator {
    private LocalGenContainer container;
    private MaterialMap materialMap;
    private WorldMap worldMap;
    private LocalGenConfig config;
    private int localAreaSize;
    private LocalMap localMap;
    private Random random;
    private TreeTypeMap treeTypeMap;

    public TreesGenerator(LocalGenContainer container) {
        this.container = container;
        this.materialMap = container.getMaterialMap();
        this.treeTypeMap = new TreeTypeMap();
    }

    public Tree generateTree(String speciment, int age) throws MaterialNotFoundException {
        TreeType treeType = treeTypeMap.getTreeType(speciment);
        int materialId = materialMap.getId(treeType.getMaterialName());
        Tree tree = new Tree(speciment, 10, materialId);
        int[][][] treeBlocks = new int[1 + treeType.getCrownRadius() * 2][1 + treeType.getCrownRadius() * 2][treeType.getHeight() + treeType.getRootDepth() + 1];
        int branchesStart = treeType.getHeight() / 2 + treeType.getRootDepth();
        // stomp
        treeBlocks[treeType.getCrownRadius()][treeType.getCrownRadius()][treeType.getRootDepth()] = TreeBlocksTypeEnum.STOMP.getCode();
        // trunk
        for (int i = treeType.getRootDepth(); i < treeBlocks[0][0].length - 1; i++) {
            treeBlocks[treeType.getCrownRadius()][treeType.getCrownRadius()][i] = TreeBlocksTypeEnum.TRUNK.getCode();
        }
        // roots
        for (int i = 0; i < treeType.getRootDepth() - 1; i++) {
            treeBlocks[treeType.getCrownRadius()][treeType.getCrownRadius()][i] = TreeBlocksTypeEnum.ROOT.getCode();
        }
        // branches
        treeBlocks[treeType.getCrownRadius() - 1][treeType.getCrownRadius()][branchesStart] = TreeBlocksTypeEnum.BRANCH.getCode();
        treeBlocks[treeType.getCrownRadius() + 1][treeType.getCrownRadius()][branchesStart] = TreeBlocksTypeEnum.BRANCH.getCode();
        treeBlocks[treeType.getCrownRadius()][treeType.getCrownRadius() - 1][branchesStart] = TreeBlocksTypeEnum.BRANCH.getCode();
        treeBlocks[treeType.getCrownRadius()][treeType.getCrownRadius() + 1][branchesStart] = TreeBlocksTypeEnum.BRANCH.getCode();
        //crown
        for (int x = 0; x < treeBlocks.length; x++) {
            for (int y = 0; y < treeBlocks[0].length; y++) {
                for (int z = branchesStart + 1; z < treeBlocks[0][0].length; z++) {
                    treeBlocks[x][y][z] = TreeBlocksTypeEnum.CROWN.getCode();
                }
            }
        }
        tree.setBlockTypes(treeBlocks);
        tree.setStompZ(treeType.getRootDepth());
        return tree;
    }
}
