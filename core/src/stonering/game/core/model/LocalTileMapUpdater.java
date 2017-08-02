package stonering.game.core.model;

import stonering.game.enums.BlockTypesEnum;
import stonering.game.enums.materials.Material;
import stonering.game.enums.materials.MaterialMap;

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
                    Material material = materialMap.getMaterial(localMap.getMaterial(x,y,z));
//                    localTileMap.setTile(x,y,z, BlockTypesEnum.getType(localMap.getBlockType(x,y,z)), material.get, );
                }
            }
        }
    }
}
