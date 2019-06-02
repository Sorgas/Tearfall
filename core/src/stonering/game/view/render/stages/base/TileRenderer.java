package stonering.game.view.render.stages.base;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.designations.Designation;
import stonering.entity.local.building.BuildingBlock;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.zone.Zone;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTileMapping;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.lists.*;
import stonering.game.model.lists.tasks.TaskContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.util.Int3DBounds;
import stonering.game.view.tilemaps.LocalTileMap;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for rendering tiles.
 *
 * @author Alexander on 06.02.2019.
 */
public class TileRenderer extends Renderer {
    private LocalMap localMap;
    private LocalTileMap localTileMap;
    private PlantContainer plantContainer;
    private EntitySelector selector;
    private UnitContainer unitContainer;
    private TaskContainer taskContainer;
    private ItemContainer itemContainer;
    private ZonesContainer zonesContainer;
    private Int3DBounds visibleArea;

    private Position cachePosition;

    public TileRenderer(DrawingUtil drawingUtil, Int3DBounds visibleArea) {
        super(drawingUtil);
        this.visibleArea = visibleArea;
        GameModel model = GameMvc.instance().getModel();
        localMap = model.get(LocalMap.class);
        localTileMap = model.get(LocalTileMap.class);
        selector = model.get(EntitySelector.class);
        unitContainer = model.get(UnitContainer.class);
        taskContainer = model.get(TaskContainer.class);
        plantContainer = model.get(PlantContainer.class);
        itemContainer = model.get(ItemContainer.class);
        zonesContainer = model.get(ZonesContainer.class);
        cachePosition = new Position(0, 0, 0);
    }

    @Override
    public void render() {
        EntitySelector selector = GameMvc.instance().getModel().get(EntitySelector.class);
        for (int z = visibleArea.getMinZ(); z <= visibleArea.getMaxZ(); z++) {
            drawingUtil.shadeByZ(selector.getPosition().z - z);
            for (int x = visibleArea.getMinX(); x <= visibleArea.getMaxX(); x++) {
                for (int y = visibleArea.getMaxY(); y >= visibleArea.getMinY(); y--) {
                    drawTile(x, y, z);
                }
            }
            for (int x = visibleArea.getMinX(); x <= visibleArea.getMaxX(); x++) {
                for (int y = visibleArea.getMaxY(); y >= visibleArea.getMinY(); y--) {
                    drawAreaLabel(x, y, z);
                }
            }
        }
    }

    /**
     * Draws all content of the tile.
     * Draw order: block, water, substrate plants, plants, building, unit, items, designation.
     * //TODO refactor
     */
    private void drawTile(int x, int y, int z) {
        GameModel model = GameMvc.instance().getModel();
        cachePosition.set(x, y, z);
        //byte lightLevel = (byte) (localMap.getLight().getValue(x, y, z) + localMap.getGeneralLight().getValue(x, y, z));  //TODO limit light level
        //drawingUtil.shadeByLight(lightLevel);
        drawBlock(x, y, z);
        drawSubstrate(x, y, z);
        drawingUtil.updateColorA(0.6f);
        drawWaterBlock(x, y, z);
        drawingUtil.updateColorA(1f);
        cachePosition.set(x, y, z);
        PlantBlock block = plantContainer.getPlantBlocks().get(cachePosition);
        if (block != null)
            drawingUtil.drawSprite(drawingUtil.selectSprite(1, block.getAtlasXY()[0], block.getAtlasXY()[1]), x, y, z, selector.getPosition());
        BuildingBlock buildingBlock = model.get(BuildingContainer.class).getBuildingBlocks().get(cachePosition);
        if (buildingBlock != null)
            drawingUtil.drawSprite(drawingUtil.selectSprite(3, 0, 0), x, y, z, selector.getPosition());
        if (unitContainer != null)
            unitContainer.getUnitsInPosition(x, y, z).forEach(unit -> {
                //TODO ((RenderAspect) unit.getAspects().get(RenderAspect.NAME)).getTexture();
                drawingUtil.drawSprite(drawingUtil.selectSprite(2, 0, 0), x, y, z, selector.getPosition());
            });
        if (itemContainer != null) {
            List<Item> items = itemContainer.getItemsInPosition(x, y, z);
            if (!items.isEmpty())
                items.forEach((item) -> drawingUtil.drawSprite(drawingUtil.selectSprite(5, item.getType().atlasXY[0], item.getType().atlasXY[1]), x, y, z, selector.getPosition()));
        }
        if (taskContainer != null) {
            Designation designation = taskContainer.getDesignation(x, y, z);
            if (designation != null)
                drawingUtil.drawSprite(drawingUtil.selectSprite(4, DesignationsTileMapping.getAtlasX(designation.getType().CODE), 0), x, y, z, selector.getPosition());
        }
        if (zonesContainer != null) {
            Zone zone = zonesContainer.getZone(cachePosition);
            if (zone != null) drawingUtil.drawSprite(zone.getType().sprite, x, y, z, selector.getPosition());
        }
        drawingUtil.resetColor();
    }

    private void drawAreaLabel(int x, int y, int z) {
        if (localMap.getBlockType(x, y, z) == BlockTypesEnum.SPACE.CODE) return;
        String text = localMap.getPassage().getArea().getValue(x, y, z) + " " + localMap.getPassage().getPassage(x, y, z);
        drawingUtil.writeText(text, x, y + 1, z, selector.getPosition());
    }

    /**
     * Draws block parts. Rendering data is stored in {@link LocalTileMap}.
     * Also draws topping part of lower block.
     */
    private void drawBlock(int x, int y, int z) {
        int atlas = localTileMap.getAtlasNum(x, y, z);
        TextureRegion region = null;
        if (atlas >= 0) { // not empty cell
            region = drawingUtil.selectSprite(atlas, localTileMap.getAtlasX(x, y, z), localTileMap.getAtlasY(x, y, z));
        } else {
            int lowerAtlas;
            if (z > 0 && (lowerAtlas = localTileMap.getAtlasNum(x, y, z - 1)) >= 0) {// not empty cell lower
                region = drawingUtil.selectToping(lowerAtlas, localTileMap.getAtlasX(x, y, z - 1), localTileMap.getAtlasY(x, y, z - 1));
            }
        }
        if (region != null) drawingUtil.drawSprite(region, x, y, z, selector.getPosition());
    }

    private void drawSubstrate(int x, int y, int z) {
        TextureRegion region = null;
        PlantBlock block = plantContainer.getSubstrateBlocks().get(cachePosition.set(x, y, z));
        if (block != null)
            region = drawingUtil.selectSprite(6, localTileMap.getAtlasX(x, y, z), block.getAtlasXY()[1]);
        else if (z > 0 && (block = plantContainer.getSubstrateBlocks().get(cachePosition.set(x, y, z - 1))) != null) {
            region = drawingUtil.selectToping(6, localTileMap.getAtlasX(x, y, z - 1), block.getAtlasXY()[1]);
        }
        if (region != null)
            drawingUtil.drawSprite(region, x, y, z, selector.getPosition());
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
}
