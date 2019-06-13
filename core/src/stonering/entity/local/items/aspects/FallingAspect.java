package stonering.entity.local.items.aspects;

import stonering.entity.local.Entity;
import stonering.entity.local.PositionAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.local.Aspect;
import stonering.entity.local.items.Item;

/**
 * Aspect for items to fall down.
 *
 * @author Savva Kodeikin
 */
public class FallingAspect extends Aspect {
    public static final String NAME = "falling";

    public FallingAspect(Entity entity) {
        super(entity);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void turn() {
        Position position = getPosition();
        if (position != null) { //TODO add aspect turn on pickup
            LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
            Position lowerPosition = Position.add(getPosition(), 0, 0, -1);
            boolean isCurrentBlockSpace = localMap.getBlockType(position) == BlockTypesEnum.SPACE.CODE;
            boolean isLowerBlockWall = localMap.getBlockType(lowerPosition) == BlockTypesEnum.WALL.CODE;

            if (localMap.inMap(lowerPosition) && isCurrentBlockSpace && !isLowerBlockWall) {
                GameMvc.instance().getModel().get(ItemContainer.class).moveItem((Item) entity, lowerPosition);
            }
        }
    }

    private Position getPosition() {
        return entity.getAspect(PositionAspect.class).position;
    }
}
