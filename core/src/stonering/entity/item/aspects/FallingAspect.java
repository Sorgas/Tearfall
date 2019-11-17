package stonering.entity.item.aspects;

import stonering.entity.Entity;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.Aspect;
import stonering.entity.item.Item;

/**
 * Aspect for item to fall down.
 *
 * @author Savva Kodeikin
 */
public class FallingAspect extends Aspect {
    public static final String NAME = "falling";

    public FallingAspect(Entity entity) {
        super(entity);
    }

    @Override
    public void turn() {
        if (entity.position != null) { //TODO add aspect turnUnit on pickup
            LocalMap localMap = GameMvc.instance().model().get(LocalMap.class);
            Position lowerPosition = Position.add(entity.position, 0, 0, -1);
            boolean isCurrentBlockSpace = localMap.getBlockType(entity.position) == BlockTypesEnum.SPACE.CODE;
            boolean isLowerBlockWall = localMap.getBlockType(lowerPosition) == BlockTypesEnum.WALL.CODE;

            if (localMap.inMap(lowerPosition) && isCurrentBlockSpace && !isLowerBlockWall) {
                GameMvc.instance().model().get(ItemContainer.class).onMapItemsSystem.changeItemPosition((Item) entity, lowerPosition);
            }
        }
    }
}
