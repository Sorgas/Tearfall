package stonering.stage.renderer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.util.geometry.Position;

import static stonering.stage.renderer.AtlasesEnum.ui_tiles;

/**
 * Renders {@link EntitySelector} sprite and frame.
 *
 * @author Alexander on 06.02.2019.
 */
public class EntitySelectorDrawer extends Drawer {
    private Position cachePosition;

    public EntitySelectorDrawer(SpriteDrawingUtil spriteDrawingUtil, ShapeDrawingUtil shapeDrawingUtil) {
        super(spriteDrawingUtil, shapeDrawingUtil);
        cachePosition = new Position();
    }

    public void render() {
        drawSelector(GameMvc.instance().model().get(EntitySelectorSystem.class).selector);
    }

    /**
     * Draws {@link EntitySelector} and selection frame;
     */
    public void drawSelector(EntitySelector selector) {
        TextureRegion region = selector.getAspect(RenderAspect.class).region;
        if(region != null) spriteUtil.drawSprite(region, selector.position.toVector3());
        //TODO add additional status sprite for selector
        //TODO add landscape dependant rendering
        SelectorBoxAspect aspect = selector.getAspect(SelectorBoxAspect.class);
        if (aspect.boxStart != null) {
            Position start = aspect.boxStart;
            int minX = Math.min(start.x, selector.position.x);
            int maxX = Math.max(start.x, selector.position.x);
            int minY = Math.min(start.y, selector.position.y);
            int maxY = Math.max(start.y, selector.position.y);
            int minZ = Math.min(start.z, selector.position.z);
            int maxZ = selector.position.z;
            for (int x = minX; x <= maxX; x++) {
                for (int y = minY; y <= maxY; y++) {
                    for (int z = minZ; z <= maxZ; z++) {
                        cachePosition.set(x, y, z);
                        if (y == maxY && z == maxZ) drawSprite(0);
                        if (y == minY && z == maxZ) drawSprite(1);
                        if (x == minX && z == maxZ) drawSprite(2);
                        if (x == maxX && z == maxZ) drawSprite(3);
                        if (y == minY && z == minZ) drawSprite(4);
                        if (y == minY && x == minX) drawSprite(5);
                        if (y == minY && x == maxX) drawSprite(6);
                        if (y == maxY && z == minZ) drawSprite(7);

                        if (x == minX && z > minZ && y == minY) drawSprite(8);
                        if (x == maxX && z > minZ && y == minY) drawSprite(9);
                        spriteUtil.updateColorA(0.5f);
                        if (z == maxZ) drawSprite(10); // top side transparent background
                        if (y == minY) drawSprite(11); // front side transparent background
                        if (z > minZ && y == minY) drawSprite(12);
                        spriteUtil.updateColorA(1f);
                    }
                }
            }
        }
    }

    private void drawSprite(int x) {
        spriteUtil.drawSprite(ui_tiles.getBlockTile(x, 1), ui_tiles, cachePosition);
    }
}
