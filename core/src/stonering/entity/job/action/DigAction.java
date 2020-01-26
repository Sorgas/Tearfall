package stonering.entity.job.action;

import stonering.entity.job.designation.OrderDesignation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.items.DiggingProductGenerator;
import stonering.util.geometry.Position;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;
import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * This action requires digging tool in performer's hands.
 * Should be only created with digging designation types.
 */
public class DigAction extends Action {
    private DesignationTypeEnum type;
    private ItemSelector toolItemSelector;

    public DigAction(OrderDesignation designation) {
        super(new PositionActionTarget(designation.position, ActionTargetTypeEnum.NEAR));
        type = designation.type;
        toolItemSelector = new ToolWithActionItemSelector("dig");
        startCondition = () -> {
            if (!type.validator.validate(actionTarget.getPosition())) return FAIL;
            EquipmentAspect equipment = task.performer.getAspect(EquipmentAspect.class);
            if (equipment == null) return FAIL;
            if (toolItemSelector.checkItems(equipment.equippedItems)) return OK;
            return addEquipAction();
        };
        onFinish = () -> {
            BlockTypesEnum oldType = GameMvc.model().get(LocalMap.class).getBlockTypeEnumValue(actionTarget.getPosition());
            if (type.validator.validate(actionTarget.getPosition())) updateMap();
            leaveStone(oldType);
            //TODO add experience
        };
    }


    private ActionConditionStatusEnum addEquipAction() {
        Item target = GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(toolItemSelector, task.performer.position);
        if (target == null) return FAIL;
        task.addFirstPreAction(new EquipItemAction(target, true));
        return NEW;
    }

    /**
     * Applies changes to local map. Some types of digging change not only target tile.
     */
    private void updateMap() {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        Position target = actionTarget.getPosition();
        switch (type) {
            case D_DIG:
                updateAndRevealMap(target, FLOOR);
                break;
            case D_STAIRS:
                if (map.getBlockType(target) == WALL.CODE) {
                    updateAndRevealMap(target, STAIRS);
                } else {
                    updateAndRevealMap(target, DOWNSTAIRS);
                }
                break;
            case D_RAMP:
                updateAndRevealMap(target, RAMP);
                Position upperPosition = new Position(target.x, target.y, target.z + 1);
                if (map.inMap(upperPosition))
                    updateAndRevealMap(upperPosition, SPACE);
                break;
            case D_CHANNEL:
                updateAndRevealMap(target, SPACE);
                Position lowerPosition = new Position(target.x, target.y, target.z - 1);
                if (map.inMap(lowerPosition) && map.getBlockType(lowerPosition) == WALL.CODE)
                    updateAndRevealMap(lowerPosition, RAMP);
        }
    }

    /**
     * Puts rock of dug material if needed.
     */
    private void leaveStone(BlockTypesEnum oldType) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        Position target = actionTarget.getPosition();
        BlockTypesEnum newType = map.getBlockTypeEnumValue(target);
        int materialId = map.getMaterial(target);
        new DiggingProductGenerator()
                .generateDigProduct(materialId, oldType, newType)
                .forEach(item -> container.onMapItemsSystem.putNewItem(item, target));
    }

    private void updateAndRevealMap(Position position, BlockTypesEnum type) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        map.setBlockType(position, type.CODE);
        map.light.handleDigging(position);
    }

    @Override
    public String toString() {
        return "Digging " + type;
    }
}
