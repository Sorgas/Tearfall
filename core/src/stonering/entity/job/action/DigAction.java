package stonering.entity.job.action;

import stonering.entity.job.action.target.ActionTarget;
import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.items.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.DiggingProductGenerator;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * This action requires digging tool in performers hands.
 * Should be only created with digging designation types.
 */
public class DigAction extends Action {
    private DesignationTypeEnum type;
    private ItemSelector toolItemSelector;

    public DigAction(OrderDesignation designation) {
        super(new PositionActionTarget(designation.getPosition(), ActionTarget.NEAR));
        type = designation.getType();
        toolItemSelector = new ToolWithActionItemSelector("dig");
    }

    @Override
    public int check() {
        if(!validate()) return FAIL;
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return FAIL;
        if (toolItemSelector.checkItems(aspect.equippedItems)) return OK;
        return addEquipAction();
    }

    private int addEquipAction() {
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().position);
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
        BlockTypesEnum blockType = BlockTypesEnum.getType(map.getBlockType(actionTarget.getPosition()));
        switch (type) {
            case DIG:
                return blockType == WALL ||
                        blockType == RAMP ||
                        blockType == STAIRS;
            case STAIRS:
                return blockType == WALL ||
                        blockType == RAMP ||
                        blockType == FLOOR ||
                        blockType == STAIRS;
            case RAMP:
                return blockType == WALL;
            case CHANNEL:
                return blockType == WALL ||
                        blockType == RAMP ||
                        blockType == FLOOR ||
                        blockType == STAIRS;
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
                updateAndRevealMap(target, FLOOR);
                break;
            case STAIRS:
                if(map.getBlockType(target) == WALL.CODE) {
                    updateAndRevealMap(target, STAIRS);
                } else {
                    updateAndRevealMap(target, DOWNSTAIRS);
                }
                break;
            case RAMP:
                updateAndRevealMap(target, RAMP);
                Position upperPosition = new Position(target.x, target.y, target.z + 1);
                if(map.inMap(upperPosition))
                    updateAndRevealMap(upperPosition, SPACE);
                break;
            case CHANNEL:
                updateAndRevealMap(target, SPACE);
                Position lowerPosition = new Position(target.x, target.y, target.z - 1);
                if(map.inMap(lowerPosition) && map.getBlockType(lowerPosition) == WALL.CODE)
                    updateAndRevealMap(lowerPosition, RAMP);
        }
    }

    /**
     * Puts rock of dug material if needed.
     */
    private void leaveStone(int material) {
        DiggingProductGenerator generator = new DiggingProductGenerator();
        if (!generator.productRequired(material)) return;
        Item item = generator.generateDigProduct(material, actionTarget.getPosition());
        GameMvc.instance().getModel().get(ItemContainer.class).addAndPut(item);
    }

    private void updateAndRevealMap(Position position, BlockTypesEnum type) {
        LocalMap map = GameMvc.instance().getModel().get(LocalMap.class);
        map.setBlockType(position, type.CODE);
        map.light.handleDigging(position);
    }

    @Override
    public String toString() {
        return "Digging " + type;
    }
}
