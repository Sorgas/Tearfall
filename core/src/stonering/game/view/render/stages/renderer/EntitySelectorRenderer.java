package stonering.game.view.render.stages.renderer;

import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.util.geometry.Position;

import javax.xml.transform.Source;

import static stonering.game.view.render.stages.renderer.AtlasesEnum.ui_tiles;

/**
 * Renders {@link EntitySelector} sprite and frame.
 *
 * @author Alexander on 06.02.2019.
 */
public class EntitySelectorRenderer extends Renderer {
    private Position cachePosition;

    public EntitySelectorRenderer(DrawingUtil drawingUtil) {
        super(drawingUtil);
        cachePosition = new Position();
    }

    @Override
    public void render() {
        drawSelector(GameMvc.instance().getModel().get(EntitySelector.class));
    }

    /**
     * Draws {@link EntitySelector} and selection frame;
     *
     * @param selector
     */
    public void drawSelector(EntitySelector selector) {
        util.drawSprite(selector.getSelectorSprite(), selector.getPosition());
        if (selector.getStatusSprite() != null) {
            util.drawSprite(selector.getStatusSprite(), selector.getPosition());
        }

        //TODO add landscape dependant rendering
        if (selector.getFrameStart() != null) {
            int minX = Math.min(selector.getFrameStart().getX(), selector.getPosition().getX());
            int maxX = Math.max(selector.getFrameStart().getX(), selector.getPosition().getX());
            int minY = Math.min(selector.getFrameStart().getY(), selector.getPosition().getY());
            int maxY = Math.max(selector.getFrameStart().getY(), selector.getPosition().getY());
            int minZ = Math.min(selector.getFrameStart().getZ(), selector.getPosition().getZ());
            int maxZ = selector.getPosition().getZ();
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
                        util.updateColorA(0.5f);
                        if (z == maxZ) drawSprite(10); // top side transparent background
                        if (y == minY) drawSprite(11); // front side transparent background
                        if (z > minZ && y == minY) drawSprite(12);
                        util.updateColorA(1f);
                    }
                }
            }
        }
    }

    private void drawSprite(int x) {
        util.drawSprite(util.selectSprite(ui_tiles, x, 1), cachePosition);
    }
}
