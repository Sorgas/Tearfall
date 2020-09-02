package stonering.entity.job.action;

import static stonering.entity.job.action.ActionConditionStatusEnum.FAIL;
import static stonering.entity.job.action.ActionConditionStatusEnum.OK;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import stonering.entity.building.Building;
import stonering.entity.building.BuildingBlock;
import stonering.entity.building.aspects.DinningTableFurnitureAspect;
import stonering.entity.building.aspects.SitFurnitureAspect;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.FoodItemAspect;
import stonering.entity.job.action.equipment.obtain.ObtainItemAction;
import stonering.entity.job.action.target.EntityActionTarget;
import stonering.entity.job.action.target.ItemActionTarget;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.enums.OrientationEnum;
import stonering.enums.unit.need.NeedEnum;
import stonering.enums.unit.need.hunger.FoodNeed;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.building.BuildingContainer;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.geometry.Position;
import stonering.util.lang.Pair;

/**
 * Action for consuming edible items and satisfying {@link FoodNeed}.
 * Will create pre actions for using table, chair, dishes, if some available.
 * If table is occupied by dirty dishes, it will be put off.
 * TODO all dishes and table behaviour is post-MVP
 *
 * @author Alexander on 30.09.2019.
 */
public class EatAction extends Action {
    private EntityActionTarget entityTarget;
    public Item item;
    private BuildingContainer container = GameMvc.model().get(BuildingContainer.class);
    public BuildingBlock tableBlock;
    public Building chair;
    public boolean started = false; // used in unit drawer

    public EatAction(Item item) {
        super(new ItemActionTarget(item));
        entityTarget = (EntityActionTarget) target;
        this.item = item;
        startCondition = () -> {
            if(!item.has(FoodItemAspect.class)) return FAIL; // item is not food

            if(!task.performer.get(EquipmentAspect.class).items.contains(item))
                return addPreAction(new ObtainItemAction(item)); // find item

            // find and move to chair
            Pair<BuildingBlock, Building> pair = findTableWithChair();
            if(pair != null) {
                tableBlock = pair.key;
                chair = pair.value;
                if(!task.performer.position.equals(chair.position)) return addPreAction(new MoveAction(chair.position));
            }

            EquipmentAspect equipment = task.performer.get(EquipmentAspect.class);
            if (equipment != null && equipment.items.contains(item)) return OK;
            return OK;
        };

        onStart = () -> {
            if (chair != null && tableBlock != null) chair.occupied = true;
//            speed = 1f * task.performer.get(HealthAspect.class).properties.get("performance");
//            maxProgress = 400;
        };

        onFinish = () -> {
            if(chair != null && tableBlock != null) chair.occupied = false;
            task.performer.get(NeedAspect.class).needs.get(NeedEnum.FOOD).changeValue(-item.get(FoodItemAspect.class).nutrition);
            GameMvc.model().get(ItemContainer.class).removeItem(item);
        };
    }

    /**
     * Looks for table with adjacent chair on map.
     */
    private Pair<BuildingBlock, Building> findTableWithChair() {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        return container.stream()
                .filter(building -> building.has(DinningTableFurnitureAspect.class)) // building is table
                .filter(table -> map.passageMap.inSameArea(table.position, task.performer.position)) // table accessible
                .flatMap(building -> Arrays.stream(building.blocks).flatMap(Arrays::stream)) // get table blocks
                .map(tableBlock -> new Pair<>(tableBlock, getChairNearTable(tableBlock))) // get free chair
                .filter(pair -> pair.getValue() != null) // chair exists
                .findFirst().orElse(null);
    }

    private Building getChairNearTable(BuildingBlock tableBlock) {
        return Stream.of(
                checkChairAtPosition(Position.add(tableBlock.position, 1, 0, 0), OrientationEnum.W),
                checkChairAtPosition(Position.add(tableBlock.position, -1, 0, 0), OrientationEnum.E),
                checkChairAtPosition(Position.add(tableBlock.position, 0, 1, 0), OrientationEnum.S),
                checkChairAtPosition(Position.add(tableBlock.position, 0, -1, 0), OrientationEnum.N))
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }

    private Building checkChairAtPosition(Position position, OrientationEnum orientation) {
        return Optional.ofNullable(container.getBuilding(position))
                .filter(building -> building.has(SitFurnitureAspect.class))
                .filter(building -> !building.occupied)
                .filter(building -> building.orientation == orientation)
                .orElse(null);
    }
}
