package stonering.stage.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
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
    private Int3dBounds bounds;

    public EntitySelectorDrawer(SpriteDrawingUtil spriteDrawingUtil, ShapeDrawingUtil shapeDrawingUtil) {
        super(spriteDrawingUtil, shapeDrawingUtil);
        bounds = new Int3dBounds();
    }

    public void render() {
        drawSelector(GameMvc.model().get(EntitySelectorSystem.class).selector);
        drawFrame(GameMvc.model().get(EntitySelectorSystem.class).selector);
    }

    public void drawSelector(EntitySelector selector) {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        TextureRegion region = selector.getAspect(RenderAspect.class).region;
        if (region == null || aspect.boxStart != null) return;
        spriteUtil.drawSprite(region, selector.position.toVector3());
    }

    private void drawFrame(EntitySelector selector) {
        SelectionAspect aspect = selector.getAspect(SelectionAspect.class);
        if (aspect.boxStart == null) return;
        bounds.set(aspect.boxStart, selector.position);
        bounds.maxZ = selector.position.z;
        bounds.iterator.accept(pos -> {
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
