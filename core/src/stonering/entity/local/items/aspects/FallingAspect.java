package stonering.entity.local.items.aspects;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.Item;

/**
 * Aspect for items to fall down.
 *
 * @author Savva Kodeikin
 */
public class FallingAspect extends Aspect {
    public static final String NAME = "falling";

    private LocalMap localMap;

    public FallingAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
    }

    @Override
    public void init() {
        super.init();
        localMap = GameMvc.instance().getModel().get(LocalMap.class);
    }

    @Override
    public void turn() {
        Position position = aspectHolder.getPosition();
        if (position != null) { //TODO add aspect turn on pickup
            Position lowerPosition = Position.add(aspectHolder.getPosition(), 0, 0, -1);
            boolean isCurrentBlockSpace = localMap.getBlockType(position) == BlockTypesEnum.SPACE.CODE;
            boolean isLowerBlockWall = localMap.getBlockType(lowerPosition) == BlockTypesEnum.WALL.CODE;

            if (localMap.inMap(lowerPosition) && isCurrentBlockSpace && !isLowerBlockWall) {
                GameMvc.instance().getModel().get(ItemContainer.class).moveItem((Item) aspectHolder, lowerPosition);
            }
        }
    }
}
