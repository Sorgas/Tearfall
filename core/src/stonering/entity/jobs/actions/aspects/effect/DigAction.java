package stonering.entity.jobs.actions.aspects.effect;

import stonering.designations.OrderDesignation;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.EquipItemAction;
import stonering.entity.jobs.actions.aspects.target.PositionActionTarget;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.entity.local.items.selectors.ToolWithActionItemSelector;
import stonering.entity.local.unit.aspects.EquipmentAspect;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.core.model.LocalMap;
import stonering.generators.items.DiggingProductGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

public class DigAction extends Action {
    private DesignationTypeEnum type;
    private ItemSelector toolItemSelector;

    public DigAction(OrderDesignation designation) {
        type = designation.getType();
        actionTarget = new PositionActionTarget(this, designation.getPosition(), false, true);
        toolItemSelector = new ToolWithActionItemSelector("dig");
    }

    @Override
    public boolean check() {
        EquipmentAspect aspect = (EquipmentAspect) task.getPerformer().getAspects().get("equipment");
        if (aspect != null) {
            return toolItemSelector.check(aspect.getEquippedItems()) || addEquipAction();
        }
        return false;
    }

    private boolean addEquipAction() {
        Item target = gameMvc.getModel().getItemContainer().getItemAvailableBySelector(toolItemSelector, task.getPerformer().getPosition());
        if (target != null) {
            task.addFirstPreAction(new EquipItemAction(target));
            return true;
        }
        return false;
    }

    @Override
    public boolean perform() {
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
        leaveStone(gameMvc.getModel().getLocalMap().getMaterial(actionTarget.getPosition()));
        return true;
    }

    private void validateAndChangeBlock(Position pos, BlockTypesEnum type) {
        boolean valid = false;
        LocalMap map = gameMvc.getModel().getLocalMap();
        switch (type) {
            case RAMP:
            case STAIRS:
                valid = map.getBlockType(pos) == BlockTypesEnum.WALL.getCode();
                break;
            case FLOOR:
                valid = map.getBlockType(pos) == BlockTypesEnum.WALL.getCode() ||
                        map.getBlockType(pos) == BlockTypesEnum.RAMP.getCode() ||
                        map.getBlockType(pos) == BlockTypesEnum.STAIRS.getCode();
                break;
            case SPACE:
                valid = true;
        }
        if (valid) {
            map.setBlockType(pos, type.getCode());
        }
    }

    /**
     * Puts rock of dug material if needed.
     *
     * @param material
     */
    private void leaveStone(int material) {
        DiggingProductGenerator generator = new DiggingProductGenerator();
        if (generator.productRequired(material))
            gameMvc.getModel().getItemContainer().addItem(generator.generateDigProduct(material), actionTarget.getPosition());
    }

    private void logStart() {
        TagLoggersEnum.TASKS.logDebug("digging " + type + " started at " + actionTarget.getPosition() + " by " + task.getPerformer().toString());
    }
}
