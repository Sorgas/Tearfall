package stonering.stage.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

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

    public EntitySelectorDrawer(SpriteDrawingUtil spriteDrawingUtil, ShapeDrawingUtil shapeDrawingUtil) {
        super(spriteDrawingUtil, shapeDrawingUtil);
        bounds = new Int3dBounds();
        cachePosition = new Position(); 
    }

    public void render() {
        drawSelector(GameMvc.model().get(EntitySelectorSystem.class).selector);
        drawFrame(GameMvc.model().get(EntitySelectorSystem.class).selector);
    }

    public void drawSelector(EntitySelector selector) {
        BoxSelectionAspect box = selector.get(BoxSelectionAspect.class);
        TextureRegion region = selector.get(RenderAspect.class).region;
        if (region == null || box.boxStart != null) return;
        spriteUtil.drawSprite(region, selector.position.toVector3());
    }

    private void defineBounds(EntitySelector selector) {
        BoxSelectionAspect box = selector.get(BoxSelectionAspect.class);
        bounds.set(selector.position, cachePosition.set(selector.position).add(selector.size));
        bounds.extendTo(box.boxStart);
        bounds.extendTo(cachePosition.set(box.boxStart).add(selector.size));
    }
    
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

    private void drawSprite(int x, Position position) {
        spriteUtil.drawSprite(ui_tiles.getBlockTile(x, 1), ui_tiles, position);
    }
}
