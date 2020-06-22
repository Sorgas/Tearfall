package stonering.game.model.system.unit;

import com.sun.istack.NotNull;
import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.item.ItemContainer;
import stonering.util.logging.Logger;

/**
 * System for updating items in creature equipment, and modifying equipment.
 * All methods are atomic. More complex manipulations with item should be implemented with {@link stonering.entity.job.action.Action}s.
 * Does not takes or puts items to map or containers, this should be done by actions.
 * <p>
 * Creature have slots for wear with different layers of wears, slots for tools and other items(grab slots).
 * To equip some wear item or do any other manipulation with equipment, unit should has at least one grab slot free.
 * Unit hauls items in grab slots.
 * TODO add item requirements (1/2 hands)
 * TODO add layers
 * 
 * @author Alexander on 06.02.2020.
 */
public class CreatureEquipmentSystem extends EntitySystem<Unit> {
    private ItemContainer container;

    public CreatureEquipmentSystem() {
        targetAspects.add(EquipmentAspect.class);
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Unit entity) {
        //TODO roll time for equipped items
    }

    public boolean canPickUpItem(EquipmentAspect equipment, Item item) {
        return equipment.grabSlots.values().stream().filter(GrabEquipmentSlot::grabFree).count() >= 1;
    }
    
    public GrabEquipmentSlot getSlotForPickingUpItem(EquipmentAspect equipment, Item item) {
        return equipment.grabSlots.values().stream().filter(GrabEquipmentSlot::grabFree).findFirst().orElse(null);
    }
    
    public void fillSlot(EquipmentAspect equipment, EquipmentSlot slot, @NotNull Item item) {
        if(item.type.has(WearAspect.class)) {
            slot.item = item; // add to wear slot
            itemContainer().equippedItemsSystem.itemEquipped(item, equipment);
        } else {
            Logger.EQUIPMENT.logError("Target item " + item + "is not wear ");
        }
    }

    public void fillGrabSlot(EquipmentAspect equipment, GrabEquipmentSlot slot, @NotNull Item item) {
        slot.grabbedItem = item; // add to wear slot
        itemContainer().equippedItemsSystem.itemEquipped(item, equipment);
    }
    
    public Item freeSlot(EquipmentSlot slot) {
        Item item = slot.item;
        slot.item = null;
        if (item != null) itemContainer().equippedItemsSystem.itemUnequipped(item);
        return item;
    }

    public Item freeGrabSlot(GrabEquipmentSlot slot) {
        Item item = slot.grabbedItem;
        slot.grabbedItem = null;
        if (item != null) itemContainer().equippedItemsSystem.itemUnequipped(item);
        return item;
    }

    private ItemContainer itemContainer() {
        return container == null ? container = GameMvc.model().get(ItemContainer.class) : container;
    }
}
