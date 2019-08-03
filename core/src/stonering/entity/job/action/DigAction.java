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
        logStart();
        Position pos = actionTarget.getPosition();
        switch (type) {
            case DIG: {
                validateAndChangeBlock(pos, FLOOR);
                break;
            }
            case RAMP: {
                validateAndChangeBlock(pos, RAMP);
                validateAndChangeBlock(new Position(pos.getX(), pos.getY(), pos.getZ() + 1), SPACE);
                break;
            }
            case STAIRS: {
                validateAndChangeStairs(pos);
                break;
            }
            case CHANNEL: {
                validateAndChangeBlock(pos, SPACE);
                validateAndChangeBlock(new Position(pos.getX(), pos.getY(), pos.getZ() - 1), RAMP);
                break;
            }
        }
        leaveStone(GameMvc.instance().getModel().get(LocalMap.class).getMaterial(actionTarget.getPosition()));
    }

    private void validateAndChangeBlock(Position pos, BlockTypesEnum type) {
        boolean valid = false;
        LocalMap map = GameMvc.instance().getModel().get(LocalMap.class);
        switch (type) {
            case RAMP:
                valid = map.getBlockType(pos) == WALL.CODE;
                break;
            case FLOOR:
                valid = map.getBlockType(pos) == WALL.CODE ||
                        map.getBlockType(pos) == RAMP.CODE ||
                        map.getBlockType(pos) == STAIRS.CODE;
                break;
            case SPACE:
                valid = true;
        }
        if (valid) map.setBlockType(pos, type.CODE);
    }

    /**
     * Stairs and stairfloor are created with the same designation, depending on block type.
     */
    private void validateAndChangeStairs(Position pos) {
        LocalMap map = GameMvc.instance().getModel().get(LocalMap.class);
        switch (BlockTypesEnum.getType(map.getBlockType(pos))) {
            case WALL:
                map.setBlockType(pos, FLOOR.CODE);
                return;
            case FLOOR:
            case RAMP:
            case FARM:
                map.setBlockType(pos, STAIRFLOOR.CODE);
                return;
        }
    }

    /**
     * Puts rock of dug material if needed.
     *
     * @param material
     */
    private void leaveStone(int material) {
        DiggingProductGenerator generator = new DiggingProductGenerator();
        if (!generator.productRequired(material)) return;
        Item item = generator.generateDigProduct(material);
        item.setPosition(actionTarget.getPosition());
        GameMvc.instance().getModel().get(ItemContainer.class).addItem(item);
    }

    private void logStart() {
        Logger.TASKS.logDebug("digging " + type + " started at " + actionTarget.getPosition() + " by " + task.getPerformer().toString());
    }

    @Override
    public String toString() {
        return "Digging name";
    }
}
