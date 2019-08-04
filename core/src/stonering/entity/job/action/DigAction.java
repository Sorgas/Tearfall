package stonering.entity.job.action;

import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.DiggingProductGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * This action requires digging tool in performers hands.
 * Should be only created with digging designation types.
 */
public class DigAction extends Action {
    private DesignationTypeEnum type;
    private ItemSelector toolItemSelector;

    public DigAction(OrderDesignation designation) {
        super(new PositionActionTarget(designation.getPosition(), false, true));
        type = designation.getType();
        toolItemSelector = new ToolWithActionItemSelector("dig");
    }

    @Override
    public int check() {
        if(!validate()) return FAIL;
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return FAIL;
        if (toolItemSelector.checkItems(aspect.getEquippedItems())) return OK;
        return addEquipAction();
    }

    private int addEquipAction() {
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return FAIL;
        task.addFirstPreAction(new EquipItemAction(target, true));
        return NEW;
    }

    @Override
    protected void performLogic() {
        if(validate()) updateMap();
        leaveStone(GameMvc.instance().getModel().get(LocalMap.class).getMaterial(actionTarget.getPosition()));
    }

    /**
     * Checks that target block can be changed to a type.
     */
    private boolean validate() {
        LocalMap map = GameMvc.instance().getModel().get(LocalMap.class);
        byte blockType = map.getBlockType(actionTarget.getPosition());
        switch (type) {
            case DIG:
                return blockType == WALL.CODE ||
                        blockType == RAMP.CODE ||
                        blockType == STAIRS.CODE;
            case STAIRS:
                return blockType == WALL.CODE ||
                        blockType == RAMP.CODE ||
                        blockType == FLOOR.CODE ||
                        blockType == STAIRS.CODE;
            case RAMP:
                return blockType == WALL.CODE;
            case CHANNEL:
                return blockType == WALL.CODE ||
                        blockType == RAMP.CODE ||
                        blockType == FLOOR.CODE ||
                        blockType == STAIRS.CODE;
        }
        return false;
    }

    /**
     * Applies changes to local map.
     */
    private void updateMap() {
        LocalMap map = GameMvc.instance().getModel().get(LocalMap.class);
        Position target = actionTarget.getPosition();
        switch(type) {
            case DIG:
                map.setBlockType(target, FLOOR.CODE);
                break;
            case STAIRS:
                if(map.getBlockType(target) == WALL.CODE) {
                    map.setBlockType(target, STAIRS.CODE);
                } else {
                    map.setBlockType(target, STAIRFLOOR.CODE);
                }
                break;
            case RAMP:
                map.setBlockType(target, RAMP.CODE);
                Position upperPosition = new Position(target.x, target.y, target.z + 1);
                if(map.inMap(upperPosition))
                    map.setBlockType(upperPosition, SPACE.CODE);
                break;
            case CHANNEL:
                map.setBlockType(target, SPACE.CODE);
                Position lowerPosition = new Position(target.x, target.y, target.z - 1);
                if(map.inMap(lowerPosition) && map.getBlockType(lowerPosition) == WALL.CODE)
                    map.setBlockType(lowerPosition, RAMP.CODE);
        }
    }

    /**
     * Puts rock of dug material if needed.
     */
    private void leaveStone(int material) {
        DiggingProductGenerator generator = new DiggingProductGenerator();
        if (!generator.productRequired(material)) return;
        Item item = generator.generateDigProduct(material);
        item.setPosition(actionTarget.getPosition());
        GameMvc.instance().getModel().get(ItemContainer.class).addItem(item);
    }

    @Override
    public String toString() {
        return "Digging " + type;
    }
}
