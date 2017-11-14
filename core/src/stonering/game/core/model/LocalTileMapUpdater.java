package stonering.game.core.model;

import stonering.enums.blocks.BlocksTileMapping;
import stonering.enums.materials.MaterialMap;

/**
 * Created by Alexander on 03.08.2017.
 */
public class LocalTileMapUpdater {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private MaterialMap materialMap;

    public void flushLocalMap() {
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
//                    Material material = materialMap.getTreeType(localMap.getTreeType(x, y, z));
//                    System.out.println(x + " " + y + " " + z + " " + localMap.getTreeType(x, y, z));
                    updateTile(x, y, z, localMap.getBlockType(x, y, z), localMap.getMaterial(x, y, z));
                }
            }
        }
    }

    public void updateTile(int x, int y, int z, byte blockType, int material) {
        if (blockType == 0) {
            localTileMap.setTile(x, y, z, (byte) 0, (byte) 0, null);
        } else {
            localTileMap.setTile(x, y, z, BlocksTileMapping.getType(blockType).getAtlasX(), materialMap.getMaterial(material).getAtlasY(), materialMap.getMaterial(material).getColor());
        }
    }

    public void setLocalTileMap(LocalTileMap localTileMap) {
        this.localTileMap = localTileMap;
    }

    public void setMaterialMap(MaterialMap materialMap) {
        this.materialMap = materialMap;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }
}
