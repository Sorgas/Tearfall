package stonering.stage.renderer;

import java.util.Optional;

import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.task.TaskContainer;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 7/28/2020
 */
public class DesignationDrawer extends Drawer {
    private TaskContainer container;
    private Position cachePosition;

    public DesignationDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        container = GameMvc.model().get(TaskContainer.class);
        cachePosition = new Position();
    }

    public void draw(int x, int y, int z) {
        Optional.ofNullable(container.designations.get(cachePosition.set(x, y, z)))
                .map(designation -> designation.get(RenderAspect.class))
                .ifPresent(aspect -> spriteUtil.drawSprite(aspect.region, cachePosition));
    }
}
