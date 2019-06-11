package stonering.game.view.render.stages.renderer;

import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;

/**
 * Renders {@link EntitySelector} sprite and frame.
 *
 * @author Alexander on 06.02.2019.
 */
public class EntitySelectorRenderer extends Renderer {

    public EntitySelectorRenderer(DrawingUtil drawingUtil) {
        super(drawingUtil);
    }

    @Override
    public void render() {
        drawSelector(GameMvc.instance().getModel().get(EntitySelector.class));
    }

    /**
     * Draws {@link EntitySelector} and selection frame;
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

                        if (y == maxY && z == maxZ)
                            util.drawSprite(util.selectSprite(4, 0, 1), x, y, z);
                        if (y == minY && z == maxZ)
                            util.drawSprite(util.selectSprite(4, 1, 1), x, y, z);
                        if (x == minX && z == maxZ)
                            util.drawSprite(util.selectSprite(4, 2, 1), x, y, z);
                        if (x == maxX && z == maxZ)
                            util.drawSprite(util.selectSprite(4, 3, 1), x, y, z);
                        if (y == minY && z == minZ)
                            util.drawSprite(util.selectSprite(4, 4, 1), x, y, z);
                        if (y == minY && x == minX)
                            util.drawSprite(util.selectSprite(4, 5, 1), x, y, z);
                        if (y == minY && x == maxX)
                            util.drawSprite(util.selectSprite(4, 6, 1), x, y, z);
                        if (y == maxY && z == minZ)
                            util.drawSprite(util.selectSprite(4, 7, 1), x, y, z);
                        if (x == minX && z > minZ && y == minY)
                            util.drawSprite(util.selectSprite(4, 8, 1), x, y, z);
                        if (x == maxX && z > minZ && y == minY)
                            util.drawSprite(util.selectSprite(4, 9, 1), x, y, z);
                        util.updateColorA(0.5f);
                        if (z == maxZ)
                            util.drawSprite(util.selectSprite(4, 10, 1), x, y, z);
                        if (y == minY)
                            util.drawSprite(util.selectSprite(4, 11, 1), x, y, z);
                        if (z > minZ && y == minY)
                            util.drawSprite(util.selectSprite(4, 12, 1), x, y, z);
                        util.updateColorA(1f);
                    }
                }
            }
        }
    }
}
