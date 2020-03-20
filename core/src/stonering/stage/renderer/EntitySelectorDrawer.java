package stonering.stage.renderer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

import static stonering.stage.renderer.AtlasesEnum.ui_tiles;

/**
 * Renders {@link EntitySelector} sprite and frame.
 * Draws sprite only if frame not started.
 * TODO add additional status sprite for selector
 * TODO add landscape dependant rendering
 * TODO support multitile sprites
 *
 * @author Alexander on 06.02.2019.
 */
public class EntitySelectorDrawer extends Drawer {
    private Position cachePosition;
    private Int3dBounds bounds;
    private Color INVALID = new Color(1, 0.8f, 0.8f, 0.5f);
    private Color VALID = new Color(0.8f, 1, 0.8f, 0.5f);

    public EntitySelectorDrawer(SpriteDrawingUtil spriteDrawingUtil, ShapeDrawingUtil shapeDrawingUtil) {
        super(spriteDrawingUtil, shapeDrawingUtil);
        bounds = new Int3dBounds();
        cachePosition = new Position();
    }

    public void render() {
        EntitySelector selector = GameMvc.model().get(EntitySelectorSystem.class).selector;
        drawValidationBackground(selector);
        drawSelectorSprites(selector);
        drawFrame(selector);
    }

    /**
     * Draws sprite defined in selector position. If selection frame exists, it is filled with sprites.
     */
    private void drawSelectorSprites(EntitySelector selector) {
        defineBounds(selector);
        TextureRegion region = selector.get(RenderAspect.class).region;
        for (int x = bounds.minX; x <= bounds.maxX; x += selector.size.x) {
            for (int y = bounds.maxY - selector.size.y + 1; y >= bounds.minY; y -= selector.size.y) {
                spriteUtil.drawSprite(region, x, y, selector.position.z);
            }
        }
    }

    /**
     * Draws background validation tiles for selection area if position validator is specified.
     */
    private void drawValidationBackground(EntitySelector selector) {
        //TODO add sprites
        PositionValidator validator = selector.get(SelectionAspect.class).tool.validator;
        if (validator == null) return;
        for (int x = selector.position.x; x < selector.position.x + selector.size.x; x++) {
            for (int y = selector.position.y; y < selector.position.y + selector.size.y; y++) {
                cachePosition.set(x, y, selector.position.z);
                spriteUtil.setColor(validator.apply(cachePosition) ? VALID : INVALID);
                drawSprite(10, cachePosition); // top side transparent background
            }
        }
    }
    
    /**
     * Draws frame of entity selector if it exists.
     */
    private void drawFrame(EntitySelector selector) {
        BoxSelectionAspect box = selector.get(BoxSelectionAspect.class);
        if (box.boxStart == null) return;
        defineBounds(selector);
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

    /**
     * Updates bounds to draw in. As selector can have non-1 size, and its position and box start position point to lower left corner of selector.
     */
    private void defineBounds(EntitySelector selector) {
        bounds.set(selector.position, selector.getOppositePosition());
        BoxSelectionAspect box = selector.get(BoxSelectionAspect.class);
        if(box.boxStart == null) return;
        bounds.extendTo(box.boxStart);
        bounds.extendTo(cachePosition.set(box.boxStart).add(selector.size.x - 1, selector.size.y - 1, 0));
    }

    private void drawSprite(int x, Position position) {
        spriteUtil.drawSprite(ui_tiles.getBlockTile(x, 1), ui_tiles, position);
    }
}
