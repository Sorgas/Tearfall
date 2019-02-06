package stonering.game.core.view.render.stages.base;

import stonering.game.core.model.EntitySelector;
import stonering.game.core.model.GameModel;

/**
 * @author Alexander on 06.02.2019.
 */
public class EntitySelectorRenderer extends Renderer {

    public EntitySelectorRenderer(GameModel gameModel, DrawingUtil drawingUtil) {
        super(gameModel, drawingUtil);
    }

    @Override
    public void render() {
        drawSelector(gameModel.get(EntitySelector.class));
    }

    public void drawSelector(EntitySelector selector) {
        drawingUtil.drawSprite(selector.getSelectorSprite(), selector.getPosition(), selector.getPosition());
        if (selector.getStatusSprite() != null) {
            drawingUtil.drawSprite(selector.getStatusSprite(), selector.getPosition(), selector.getPosition());
        }
    }
}
