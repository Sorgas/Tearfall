package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.tool.BuildingSelectionTool;
import stonering.game.model.entity_selector.tool.SelectionTool;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

import static stonering.enums.blocks.BlockTypeEnum.*;
import static stonering.stage.renderer.atlas.AtlasesEnum.ui_tiles;

/**
 * Renders {@link EntitySelector} sprite and frame.
 * Draws selector sprite in selector position. Sprite can be larger than 1 tile. Sprite not drawn if selection frame exists.
 * Draws selection frame if it exists. Selector size is always 1 when frame started.
 * Draws validation background if {@link SelectionTool#validator} exists. Validation layer drawn both for frame and single place selection.
 * TODO add landscape dependant rendering
 *
 * @author Alexander on 06.02.2019.
 */
public class EntitySelectorDrawer extends Drawer {
    private LocalMap map;
    private EntitySelector selector;
    private Position cachePosition;
    private Int3dBounds bounds;
    private Color INVALID = new Color(1, 0.8f, 0.8f, 0.5f);
    private Color VALID = new Color(0.8f, 1, 0.8f, 0.5f);
    private Color WHITE = new Color(1, 1, 1, 1);
    private Color TRANSPARENT_WHITE = new Color(1, 1, 1, 0.5f);

    public EntitySelectorDrawer(SpriteDrawingUtil spriteDrawingUtil, ShapeDrawingUtil shapeDrawingUtil) {
        super(spriteDrawingUtil, shapeDrawingUtil);
        bounds = new Int3dBounds();
        cachePosition = new Position();
        map = GameMvc.model().get(LocalMap.class);
        selector = GameMvc.model().get(EntitySelectorSystem.class).selector;
    }

    public void draw() {
        drawSelectorAdditionalSprites();
//        drawFrame();
    }

    public void render(int x, int y, int z) {
        drawSelectorSprites(x, y, z);
        drawValidationBackground(x, y, z);
    }

    /**
     * Draws sprite defined in selector position. If selection frame exists, it is filled with sprites.
     */
    private void drawSelectorSprites(int x, int y, int z) {
        if (!bounds.isIn(x, y, z)) return;
        SelectionTool tool = selector.get(SelectionAspect.class).tool;
        if (tool == SelectionTools.DESIGNATION) {
            byte blockType = map.blockType.get(x, y, selector.position.z);
            int atlasY = blockType == FLOOR.CODE
                    || blockType == DOWNSTAIRS.CODE
                    || blockType == FARM.CODE
                    || blockType == SPACE.CODE ? 1 : 0;
            TextureRegion region = ui_tiles.getBlockTile(SelectionTools.DESIGNATION.type.SPRITE_X, atlasY);
            spriteUtil.drawSprite(region, x, y, selector.position.z);
        } else {
            boolean buildingToolActive = selector.get(SelectionAspect.class).tool == SelectionTools.BUILDING;
            spriteUtil.setColor(buildingToolActive ? TRANSPARENT_WHITE : WHITE);
            TextureRegion region = selector.get(RenderAspect.class).region;
            spriteUtil.drawSprite(region, x, y, selector.position.z);
        }
    }

    private void drawSelectorAdditionalSprites() {
        SelectionTool tool = selector.get(SelectionAspect.class).tool;
        if (tool instanceof BuildingSelectionTool) { // draw access positions for buildings
            PositionValidator validator = selector.get(SelectionAspect.class).tool.validator;
            BuildingSelectionTool buildingTool = (BuildingSelectionTool) tool;
            for (IntVector2 offset : buildingTool.accessPoints) {
                cachePosition.set(selector.position).add(offset);
                spriteUtil.setColor(validator.apply(cachePosition) ? VALID : INVALID);
                spriteUtil.drawSprite(buildingTool.workbenchAccessSprite, cachePosition);
            }
        }
    }

    /**
     * Draws background validation tiles for selection area if position validator is specified.
     */
    private void drawValidationBackground(int x, int y, int z) {
        if (!bounds.isIn(x, y, z)) return;
        PositionValidator validator = selector.get(SelectionAspect.class).tool.validator;
        if (validator == null) return;
        cachePosition.set(x, y, selector.position.z);
        spriteUtil.setColor(validator.apply(cachePosition) ? VALID : INVALID);
        spriteUtil.drawSprite(ui_tiles.getBlockTile(0, 4), cachePosition);
    }

    public void defineBounds() {
        BoxSelectionAspect box = selector.get(BoxSelectionAspect.class);
        Position pos = selector.position;
        if (box.boxStart != null) { // frame
            bounds.set(pos, box.boxStart);
        } else { // single selection
            bounds.set(pos, cachePosition.set(pos).add(selector.size.x - 1, selector.size.y - 1, 0)); // size of selector itself
        }
    }

    private void drawFrame() {
        BoxSelectionAspect box = selector.get(BoxSelectionAspect.class);
        if (box.boxStart == null) return;
        defineBounds();
        bounds.maxZ = selector.position.z;
        bounds.iterate(pos -> {
            if (pos.y == bounds.maxY && pos.z == bounds.maxZ) drawSprite(0, pos);
            if (pos.y == bounds.minY && pos.z == bounds.maxZ) drawSprite(1, pos);
            if (pos.x == bounds.minX && pos.z == bounds.maxZ) drawSprite(2, pos);
            if (pos.x == bounds.maxX && pos.z == bounds.maxZ) drawSprite(3, pos);
            if (pos.y == bounds.minY && pos.z == bounds.minZ) drawSprite(4, pos);
            if (pos.y == bounds.minY && pos.x == bounds.minX) drawSprite(5, pos);
            if (pos.y == bounds.minY && pos.x == bounds.maxX) drawSprite(6, pos);
            if (pos.y == bounds.maxY && pos.z == bounds.minZ) drawSprite(7, pos);

            if (pos.x == bounds.minX && pos.z > bounds.minZ && pos.y == bounds.minY) drawSprite(8, pos);
            if (pos.x == bounds.maxX && pos.z > bounds.minZ && pos.y == bounds.minY) drawSprite(9, pos);
            spriteUtil.updateColorA(0.5f);
            if (pos.z == bounds.maxZ) drawSprite(10, pos); // top side transparent background
            if (pos.y == bounds.minY) drawSprite(11, pos); // front side transparent background
            if (pos.z > bounds.minZ && pos.y == bounds.minY) drawSprite(12, pos);
            spriteUtil.updateColorA(1f);
        });
    }

    private void drawSprite(int x, Position position) {
        spriteUtil.drawSprite(ui_tiles.getBlockTile(x, 2), position);
    }
}
