package stonering.game.core.view.render.stages.base;

import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.unit.UnitBlock;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.GameModel;
import stonering.game.core.model.lists.ItemContainer;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.view.render.util.Int3DBounds;
import stonering.game.core.view.tilemaps.LocalTileMap;

import java.util.ArrayList;

/**
 * @author Alexander on 06.02.2019.
 */
public class TileRenderer extends Renderer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private EntitySelector selector;

    public TileRenderer(GameModel gameModel, DrawingUtil drawingUtil) {
        super(gameModel, drawingUtil);
        localMap = gameModel.get(LocalMap.class);
        localTileMap = gameModel.get(LocalTileMap.class);
        selector = gameModel.get(EntitySelector.class);
    }

    @Override
    public void render() {
        drawTiles(defineframe(), gameModel.get(EntitySelector.class));
    }

    public void drawTiles(Int3DBounds bounds, EntitySelector selector) {
        for (int z = bounds.getMinZ(); z <= bounds.getMaxZ(); z++) {
            drawingUtil.shadeByZ(selector.getPosition().z - z);
            for (int x = bounds.getMinX(); x <= bounds.getMaxX(); x++) {
                for (int y = bounds.getMaxY(); y >= bounds.getMinY(); y--) {
                    drawTile(x, y, z);
                }
            }
        }
    }

    /**
     * Draws all content of the tile.
     * Draw order: block, plants, building, unit, items, designation.
     */
    private void drawTile(int x, int y, int z) {
        byte lightLevel = (byte) (localMap.getLight().getValue(x, y, z) + localMap.getGeneralLight().getValue(x, y, z));  //TODO limit light level
        drawingUtil.shadeByLight(lightLevel);
        drawBlock(x, y, z);
        drawingUtil.updateColorA(0.6f);
        drawWaterBlock(x, y, z);
        drawingUtil.updateColorA(1f);
        PlantBlock plantBlock = localMap.getPlantBlock(x, y, z);
        if (plantBlock != null)
            drawingUtil.drawSprite(drawingUtil.selectSprite(1, plantBlock.getAtlasX(), plantBlock.getAtlasY()), x, y, z, selector.getPosition());
        BuildingBlock buildingBlock = localMap.getBuildingBlock(x, y, z);
        if (buildingBlock != null)
            drawingUtil.drawSprite(drawingUtil.selectSprite(3, 0, 0), x, y, z, selector.getPosition());
        UnitBlock unitBlock = localMap.getUnitBlock(x, y, z);
        if (unitBlock != null)
            drawingUtil.drawSprite(drawingUtil.selectSprite(2, 0, 0), x, y, z, selector.getPosition());
        if(GameMvc.getInstance().getModel().get(ItemContainer.class) != null) {
            ArrayList<Item> items = GameMvc.getInstance().getModel().get(ItemContainer.class).getItems(x, y, z);
            if (!items.isEmpty())
                items.forEach((item) -> drawingUtil.drawSprite(drawingUtil.selectSprite(5, item.getType().getAtlasXY()[0], item.getType().getAtlasXY()[1]), x, y, z, selector.getPosition()));
        }
        if (localMap.getDesignatedBlockType(x, y, z) > 0)
            drawingUtil.drawSprite(drawingUtil.selectSprite(4, DesignationsTileMapping.getAtlasX(localMap.getDesignatedBlockType(x, y, z)), 0), x, y, z, selector.getPosition());
        drawingUtil.resetColor();
    }

    /**
     * Draws block parts.
     */
    private void drawBlock(int x, int y, int z) {
        int atlas = localTileMap.getAtlasNum(x, y, z);
        if (atlas >= 0) { // not empty cell
            drawingUtil.drawSprite(drawingUtil.selectSprite(atlas, localTileMap.getAtlasX(x, y, z), localTileMap.getAtlasY(x, y, z)), x, y, z, selector.getPosition());
        } else {
            int lowerAtlas;
            if (z > 0 && (lowerAtlas = localTileMap.getAtlasNum(x, y, z - 1)) >= 0) {// not empty cell lower
                drawingUtil.drawSprite(drawingUtil.selectToping(lowerAtlas, localTileMap.getAtlasX(x, y, z - 1), localTileMap.getAtlasY(x, y, z - 1)), x, y, z, selector.getPosition());
            }
        }
    }

    /**
     * Draws water in this tile.
     */
    private void drawWaterBlock(int x, int y, int z) {
        if (localMap.getFlooding(x, y, z) != 0) {
            drawingUtil.drawSprite(drawingUtil.selectSprite(0, 13 + localMap.getFlooding(x, y, z), 0), x, y, z, selector.getPosition());
        } else {
            if (z > 0 && localMap.getFlooding(x, y, z - 1) >= 7) {// not empty cell lower
                drawingUtil.drawSprite(drawingUtil.selectToping(0, 20, 0), x, y, z, selector.getPosition());
            }
        }
    }

    private Int3DBounds defineframe() {
        EntitySelector selector = gameModel.get(EntitySelector.class);
        return new Int3DBounds(Math.max(selector.getPosition().x - DrawingUtil.viewAreaWidth, 0),
                Math.max(selector.getPosition().y - DrawingUtil.viewAreaWidth, 0),
                Math.max(selector.getPosition().z - DrawingUtil.viewAreDepth, 0),
                Math.min(selector.getPosition().x + DrawingUtil.viewAreaWidth, localTileMap.getxSize() - 1),
                Math.min(selector.getPosition().y + DrawingUtil.viewAreaWidth, localTileMap.getySize() - 1),
                Math.min(selector.getPosition().z, localTileMap.getzSize() - 1));
    }
}
