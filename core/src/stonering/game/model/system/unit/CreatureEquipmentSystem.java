package stonering.game.model.system.unit;

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
import stonering.util.global.Logger;

/**
 * System for updating items in creature equipment, and modifying equipment.
 * All methods are atomic. More complex manipulations with item should be implemented with {@link stonering.entity.job.action.Action}s.
 * Does not takes or puts items to map or containers, this should be done by actions.
 * <p>
 * Creature have slots for wear with different layers of wears, slots for tools and other items(grab slots).
 * To equip some wear item or do any other manipulation with equipment, unit should has at least one grab slot free.
 * Unit hauls items in grab slots.
 *
 * @author Alexander on 06.02.2020.
 */
public class CreatureEquipmentSystem extends EntitySystem<Unit> {

    public CreatureEquipmentSystem() {
        targetAspects.add(EquipmentAspect.class);
        updateInterval = TimeUnitEnum.MINUTE;
    }

    @Override
    public void update(Unit entity) {
        //TODO roll time for equipped items
    }

    /**
     * Equips wear on body and tools into hands.
     * Does nothing target slot is occupied if some validation fails.
     * TODO slots have left/right in their names. wear items have not.
     */
    public boolean equipItem(EquipmentAspect equipment, Item item) {
        if (equipment.equippedItems.contains(item))
            return Logger.ITEMS.logError("Equipping already equipped item " + item + " to unit " + equipment.entity, false);

        // item is tool
        if (item.type.tool != null) {
            GrabEquipmentSlot emptySlot = equipment.grabSlots.values().stream().filter(slot -> slot.grabbedItem == null).findFirst().orElse(null);
            if (emptySlot == null)
                return Logger.EQUIPMENT.logError("Equipping tool " + item + " to unit " + equipment + " while no grab slots are free.", false);
            emptySlot.grabbedItem = item;
            return true;
        }

        //item is wear
        //TODO add layers check when layers will be added
        return Logger.ITEMS.logError("Attempt to equip item " + item + " as wear or tool to unit " + equipment.entity, false);
    }

    //TODO add layers
    public boolean freeSlot(EquipmentAspect equipment, String slotName) {
        EquipmentSlot slot = equipment.slots.get(slotName);
        if (slot == null)
            return Logger.EQUIPMENT.logError("Slot " + slotName + " not found on creature " + equipment.entity, false);
        if (slot.item == null)
            return Logger.EQUIPMENT.logError("Attempt to free slot " + slotName + ", which is already free", false);
        slot.item = null;
        return true;
    }

    /**
     * Finds appropriate slot for item. Item should be wear or tool.
     */
    //TODO distinguish left and right items.
    public EquipmentSlot getSlotForEquippingWear(EquipmentAspect aspect, Item item) {
        WearAspect wear = item.getAspect(WearAspect.class);
        if (wear != null && aspect.slots.get(wear.slot) != null) { // item is wear and slot exists
            return aspect.slots.get(wear.slot);
        }
        return null;
    }

    //TODO add main- and off- hand.
    public GrabEquipmentSlot getSlotForEquippingTool(EquipmentAspect equipment, Item item) {
        if (item.isTool() && !equipment.grabSlots.isEmpty())
            return equipment.grabSlots.values().stream().filter(slot -> slot.isSuitableFor(item)).findFirst().orElse(equipment.grabSlots.get(0)); // item is tool and grab slot exists.
        return null;
    }

    /**
     * Checks if creature can pick up given item with grab slots.
     * TODO add item requirements (1/2 hands)
     */
    public boolean canPickUpItem(EquipmentAspect equipment, Item item) {
        return equipment.grabSlots.values().stream().filter(slot -> slot.grabbedItem == null).count() >= 1;
    }

    /**
     * Sets item to first found one of grab slots.
     * TODO use 2 hands for large items.
     */
    public void pickUpItem(EquipmentAspect equipment, Item item) {
        equipment.grabSlots.values().stream().filter(GrabEquipmentSlot::grabFree).findFirst().ifPresent(slot -> {
            slot.grabbedItem = item;
            ItemContainer container = GameMvc.model().get(ItemContainer.class);
            container.onMapItemsSystem.removeItemFromMap(item);
            container.equippedItemsSystem.itemEquipped(item, equipment);
        });
    }

    /**
     * Equips wear item to appropriate wear slot. Item should be in some grab slot of a creature.
     */
    public boolean equipWearItem(EquipmentAspect equipment, Item item) {
        WearAspect wear = item.type.getAspect(WearAspect.class);
        if (wear == null) return Logger.EQUIPMENT.logError("Target item " + item + "is not wear ", false);
        GrabEquipmentSlot grabSlot = equipment.grabSlots.values().stream().filter(slot -> slot.grabbedItem == item).findFirst().orElse(null);
        if (grabSlot == null) return Logger.EQUIPMENT.logError("Target item " + item + "is not in grab slots ", false);
        EquipmentSlot slot = equipment.slots.get(wear.slot);
        if (slot.item != null) return Logger.EQUIPMENT.logError("Slot " + slot.name + " is occupied ", false);
        grabSlot.grabbedItem = null; // remove from hands
        slot.item = item; // add to wear slot
        return true;
    }
}
