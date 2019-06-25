package stonering.entity.job.action;

import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.local.item.Item;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.entity.local.item.selectors.ToolWithActionItemSelector;
import stonering.entity.local.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.DiggingProductGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

public class DigAction extends Action {
    private DesignationTypeEnum type;
    private ItemSelector toolItemSelector;

    public DigAction(OrderDesignation designation) {
        super(new PositionActionTarget(designation.getPosition(), false, true));
        type = designation.getType();
        toolItemSelector = new ToolWithActionItemSelector("dig");
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = task.getPerformer().getAspect(EquipmentAspect.class);
        if (aspect == null) return false;
        return toolItemSelector.check(aspect.getEquippedItems()) || addEquipAction();
    }

    private boolean addEquipAction() {
        Item target = GameMvc.instance().getModel().get(ItemContainer.class).getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target == null) return false;
        task.addFirstPreAction(new EquipItemAction(target, true));
        return true;
    }

    @Override
    protected void performLogic() {
        logStart();
        Position pos = actionTarget.getPosition();
        switch (type) {
            case DIG: {
                validateAndChangeBlock(pos, BlockTypesEnum.FLOOR);
                break;
            }
            case RAMP: {
                validateAndChangeBlock(pos, BlockTypesEnum.RAMP);
                validateAndChangeBlock(new Position(pos.getX(), pos.getY(), pos.getZ() + 1), BlockTypesEnum.SPACE);
                break;
            }
            case STAIRS: {
                validateAndChangeBlock(pos, BlockTypesEnum.STAIRS);
                break;
            }
            case CHANNEL: {
                validateAndChangeBlock(pos, BlockTypesEnum.SPACE);
                validateAndChangeBlock(new Position(pos.getX(), pos.getY(), pos.getZ() - 1), BlockTypesEnum.RAMP);
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
            case STAIRS:
                valid = map.getBlockType(pos) == BlockTypesEnum.WALL.CODE;
                break;
            case FLOOR:
                valid = map.getBlockType(pos) == BlockTypesEnum.WALL.CODE ||
                        map.getBlockType(pos) == BlockTypesEnum.RAMP.CODE ||
                        map.getBlockType(pos) == BlockTypesEnum.STAIRS.CODE;
                break;
            case SPACE:
                valid = true;
        }
        if (valid) map.setBlockType(pos, type.CODE);
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
        return "Digging action";
    }
}
