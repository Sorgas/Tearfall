package stonering.stage.renderer;

import static stonering.stage.renderer.atlas.AtlasesEnum.blocks;

import stonering.enums.blocks.BlockTileMapping;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.tilemaps.LocalTileMap;
import stonering.util.geometry.Position;

/**
 * Draws different block types. Flat and non-flat blocks are drawn separately.
 *
 * @author Alexander on 23.04.2020
 */
public class BlockDrawer extends Drawer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private Position cachePosition;

    public BlockDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        localMap = GameMvc.model().get(LocalMap.class);
        localTileMap = GameMvc.model().get(LocalTileMap.class);
        cachePosition = new Position();
    }

    public void drawFloor(int x, int y, int z) {
        cachePosition.set(x, y, z);
        BlockTypeEnum type = localMap.blockType.getEnumValue(x, y, z);
        if (type == BlockTypeEnum.SPACE && z > 0) { // draw topping for ramps below
            spriteUtil.drawSprite(blocks.getToppingTile(getAtlasXForBlock(x, y, z - 1), getAtlasYForBlock(x, y, z - 1)), cachePosition);
            return;
        }
        if (type == BlockTypeEnum.STAIRS) type = BlockTypeEnum.DOWNSTAIRS; // downstairs rendered under stairs.
        if (!type.FLAT) type = BlockTypeEnum.FLOOR; // floor is rendered under non-flat tiles.
        if (type == BlockTypeEnum.FLOOR || type == BlockTypeEnum.DOWNSTAIRS || type == BlockTypeEnum.FARM) {
            int atlasX = BlockTileMapping.getType(type.CODE).ATLAS_X;
            spriteUtil.drawSprite(blocks.getBlockTile(atlasX, getAtlasYForBlock(x, y, z)), cachePosition);
        }
    }

    public void drawBlock(int x, int y, int z) {
        cachePosition.set(x, y, z);
        BlockTypeEnum type = localMap.blockType.getEnumValue(cachePosition);
        if (!type.FLAT)
            spriteUtil.drawSprite(blocks.getBlockTile(getAtlasXForBlock(x, y, z), getAtlasYForBlock(x, y, z)), cachePosition);
    }

    /**
     * Returns atlas x for given block. Blocks and toppings have similar coordinates.
     */
    private int getAtlasXForBlock(int x, int y, int z) {
        byte blockType = localMap.blockType.get(x, y, z);
        if (blockType == BlockTypeEnum.SPACE.CODE) return -1;
        return localTileMap.get(x, y, z).x;
    }

    private int getAtlasYForBlock(int x, int y, int z) {
        return MaterialMap.getMaterial(localMap.blockType.getMaterial(x, y, z)).atlasY;
    }
}
