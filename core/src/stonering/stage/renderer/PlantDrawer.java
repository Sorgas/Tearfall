package stonering.stage.renderer;

import java.util.Optional;

import stonering.entity.RenderAspect;
import stonering.game.GameMvc;
import stonering.game.model.system.plant.PlantContainer;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 17.07.2020.
 */
public class PlantDrawer extends Drawer {
    private PlantContainer container;
    private Position cachePosition;
    
    public PlantDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        container = GameMvc.model().get(PlantContainer.class);
        cachePosition = new Position();
    }
    
    public void drawPlantBlock(int x, int y, int z) {
        Optional.ofNullable(container.getPlantBlock(cachePosition.set(x, y, z)))
                .map(block -> block.get(RenderAspect.class))
                .ifPresent(render -> spriteUtil.drawSprite(render.region, x, y, z));
    }
}
