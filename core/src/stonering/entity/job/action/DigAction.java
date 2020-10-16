package stonering.entity.job.action;

import stonering.entity.job.action.equipment.use.EquipToolItemAction;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.action.target.PositionActionTarget;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ToolWithActionItemSelector;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.action.ActionTargetTypeEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.designations.DesignationTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.task.TaskContainer;
import stonering.game.model.system.unit.UnitContainer;
import stonering.generators.items.DiggingProductGenerator;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import static stonering.entity.job.action.ActionConditionStatusEnum.*;
import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Action for digging up tiles of map. Tile opennes(part of space not occupied by solid material) can be only increased by digging.
 * Requires digging tool in performer's hands.
 * Uses mining skill increasing digging speed.
 * Should be only created with digging designation types.
 */
public class DigAction extends Action {
    private DesignationTypeEnum type;
    private final float workAmountModifier = 10f;
    private final String toolActionName = "dig";

    public DigAction(Designation designation) {
        super(new PositionActionTarget(designation.position, ActionTargetTypeEnum.NEAR), "mining");
        type = designation.type;

        startCondition = () -> {
            if (!type.VALIDATOR.apply(target.getPosition())) return FAIL; // tile still valid
            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            if (equipment == null)
                return FAIL;
            if (equipment.toolWithActionEquipped(toolActionName)) return OK; // tool already equipped
            return addEquipAction();
        };
        
        onStart = () -> {
            speed = 1 + skill().speed * performerLevel() + performance();
            maxProgress = getWorkAmount(designation) * workAmountModifier; // 480 for wall to floor in marble
        };

        onFinish = () -> {
            BlockTypeEnum oldType = GameMvc.model().get(LocalMap.class).blockType.getEnumValue(target.getPosition());
            if (!type.VALIDATOR.apply(target.getPosition())) return;
            updateMap();
            leaveStone(oldType);
            GameMvc.model().get(UnitContainer.class).experienceSystem.giveExperience(task.performer, skill);
            GameMvc.model().get(TaskContainer.class).designationSystem.removeDesignation(designation.position);
        };
    }

    private ActionConditionStatusEnum addEquipAction() {
        Item target = GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(new ToolWithActionItemSelector(toolActionName), task.performer.position);
        if (target == null)
            return FAIL; // no tool available
        return addPreAction(new EquipToolItemAction(target));
    }

    /**
     * Applies changes to local map. Some types of digging change not only target tile.
     */
    private void updateMap() {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        Position target = this.target.getPosition();
        switch (type) {
            case D_DIG:
                updateAndRevealMap(target, FLOOR);
                break;
            case D_STAIRS:
                if (map.blockType.get(target) == WALL.CODE) {
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
                Position rampPosition = new Position(target.x, target.y, target.z - 1);
                if (map.inMap(rampPosition) && map.blockType.get(rampPosition) == WALL.CODE)
                    updateAndRevealMap(rampPosition, RAMP);
                break;
            case D_DOWNSTAIRS:
                updateAndRevealMap(target, DOWNSTAIRS);
                Position stairsPosition = new Position(target.x, target.y, target.z - 1);
                if (map.inMap(stairsPosition) && map.blockType.get(stairsPosition) == WALL.CODE)
                    updateAndRevealMap(stairsPosition, STAIRS);
                break;
        }
    }
 
    /**
     * Puts rock of dug material if needed.
     */
    private void leaveStone(BlockTypeEnum oldType) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        ItemContainer container = GameMvc.model().get(ItemContainer.class);
        Position target = this.target.getPosition();
        BlockTypeEnum newType = map.blockType.getEnumValue(target);
        int materialId = map.blockType.getMaterial(target);
        new DiggingProductGenerator()
                .generateDigProduct(materialId, oldType, newType)
                .forEach(item -> container.onMapItemsSystem.addNewItemToMap(item, target));
    }

    private void updateAndRevealMap(Position position, BlockTypeEnum type) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        map.blockType.set(position, type);
        map.light.handleDigging(position);
    }

    /**
     * Work amount is based on material hardness and volume of dug stone.
     * TODO cover with test
     */
    private float getWorkAmount(Designation designation) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        switch (designation.type) {
            case D_DIG:
                return getWorkAmountForTile(designation.position, map, FLOOR);
            case D_STAIRS:
                return getWorkAmountForTile(designation.position, map, STAIRS);
            case D_DOWNSTAIRS:
                return getWorkAmountForTile(designation.position, map, DOWNSTAIRS);
            case D_RAMP:
                Position upperPosition = Position.add(designation.position, 0, 0, 1);
                return getWorkAmountForTile(designation.position, map, RAMP) + Math.max(getWorkAmountForTile(upperPosition, map, SPACE), 0);
            case D_CHANNEL:
                Position lowerPosition = Position.add(designation.position, 0, 0, -1);
                return getWorkAmountForTile(designation.position, map, SPACE) + Math.max(getWorkAmountForTile(lowerPosition, map, RAMP), 0);
        }
        return Logger.TASKS.logError("Non-digging designation type in DigAction", 0);
    }

    private float getWorkAmountForTile(Position position, LocalMap map, BlockTypeEnum targetType) {
        return MaterialMap.getMaterial(map.blockType.getMaterial(position)).density *
                (targetType.OPENNESS - map.blockType.getEnumValue(position).OPENNESS);
    }

    @Override
    public String toString() {
        return "Digging " + type;
    }
}
