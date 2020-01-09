package stonering.stage.renderer;

import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
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
        drawSelector(GameMvc.instance().model().get(EntitySelector.class));
    }

    /**
     * Draws {@link EntitySelector} and selection frame;
     *
     * @param selector
     */
    public void drawSelector(EntitySelector selector) {
        spriteUtil.drawSprite(selector.getSelectorSprite(), selector.position.toVector3());
        if (selector.getStatusSprite() != null) {
            spriteUtil.drawSprite(selector.getStatusSprite(), selector.position.toVector3());
        }

        //TODO add landscape dependant rendering
        if (selector.getFrameStart() != null) {
            int minX = Math.min(selector.getFrameStart().x, selector.position.x);
            int maxX = Math.max(selector.getFrameStart().x, selector.position.x);
            int minY = Math.min(selector.getFrameStart().y, selector.position.y);
            int maxY = Math.max(selector.getFrameStart().y, selector.position.y);
            int minZ = Math.min(selector.getFrameStart().z, selector.position.z);
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
