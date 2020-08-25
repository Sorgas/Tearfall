package stonering.enums.unit.need.wear;

import static stonering.enums.action.TaskPriorityEnum.HEALTH_NEEDS;
import static stonering.enums.action.TaskPriorityEnum.NONE;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import stonering.entity.Entity;
import stonering.entity.item.selectors.ItemSelector;
import stonering.entity.item.selectors.WearForSlotItemSelector;
import stonering.entity.job.Task;
import stonering.entity.job.action.equipment.use.EquipWearItemAction;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.MoodEffect;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.action.TaskPriorityEnum;
import stonering.enums.unit.need.Need;
import stonering.game.GameMvc;
import stonering.game.model.system.item.ItemContainer;

/**
 * Basic need for clothes. Each creature species has own list of limbs to be covered by wear.
 *
 * @author Alexander Kuzyakov on 21.09.2018.
 */
public class WearNeed extends Need {

    public WearNeed(String moodEffectKey) {
        super(moodEffectKey);
    }

    /**
     * Counts current priority for creature to find wear.
     * Having no wear only gives comfort penalty, so priority is never high.
     * TODO add prioritizing based on environment temperature
     */
    @Override
    public TaskPriorityEnum countPriority(Unit unit) {
        int emptySlots = Optional.ofNullable(unit.get(EquipmentAspect.class))
                .map(EquipmentAspect::getEmptyDesiredSlots)
                .map(Stream::count)
                .orElse(0L).intValue();
        return emptySlots != 0 ? HEALTH_NEEDS : NONE;
    }

    @Override
    public boolean isSatisfied(Unit unit) {
        return false;
    }

    /**
     * Creates task to equip wear if needed.
     */
    @Override
    public Task tryCreateTask(Unit unit) {
        return Optional.ofNullable(unit.get(EquipmentAspect.class))
                .flatMap(equipmentAspect -> equipmentAspect.getEmptyDesiredSlots()
                        .map(slot -> tryCreateEquipTask(unit, slot)) // try create tasks to fill slots
                        .filter(Objects::nonNull) // filter not created tasks
                        .findFirst())
                .orElse(null);
    }

    @Override
    public MoodEffect getMoodPenalty(Unit unit, NeedState state) {
        // TODO mood penalty for each missing items
        return null;
    }

    /**
     * Attempts to create task for equipping wear into given slot.
     * Task is not created, if item cannot be found on map.
     */
    private Task tryCreateEquipTask(Entity entity, EquipmentSlot equipmentSlot) {
        ItemSelector itemSelector = new WearForSlotItemSelector(equipmentSlot.name);
        return Optional.ofNullable(GameMvc.model().get(ItemContainer.class).util.getItemAvailableBySelector(itemSelector, entity.position))
                .map(EquipWearItemAction::new)
                .map(Task::new)
                .orElse(null);
    }
}
