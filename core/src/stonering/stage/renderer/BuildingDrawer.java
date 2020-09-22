package stonering.stage.renderer;

import stonering.entity.RenderAspect;
import stonering.entity.building.BuildingBlock;
import stonering.game.GameMvc;
import stonering.game.model.system.building.BuildingContainer;
import stonering.util.geometry.Position;

/**
 * Draws buildings. All buildings are drawn as a whole, using sprite from {@link RenderAspect}.
 * Buildings have array of {@link BuildingBlock}s, oriented in building and placed to map.
 * Left lower block of a building has 'drawn' flag, meaning building sprite should be drawn on that block.
 *
 * @author Alexander on 11.03.2020.
 */
public class BuildingDrawer extends Drawer {
    private BuildingContainer container;

    public BuildingDrawer(SpriteDrawingUtil spriteUtil, ShapeDrawingUtil shapeUtil) {
        super(spriteUtil, shapeUtil);
        container = GameMvc.model().get(BuildingContainer.class);
    }

    public void drawBuilding(Position position) {
        BuildingBlock block = container.buildingBlocks.get(position);
        if (block == null) return;
        if (block.building.blocks[0][0] != block) return; // skip not main blocks of a building
        RenderAspect aspect = block.building.get(RenderAspect.class);
        spriteUtil.drawSprite(aspect.region, block.position);
    }
}
