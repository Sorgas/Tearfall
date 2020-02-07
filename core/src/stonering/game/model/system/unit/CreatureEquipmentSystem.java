package stonering.game.model.system.unit;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.model.system.EntitySystem;
import stonering.util.global.Logger;

/**
 * System for updating items in creature equipment, and modifying equipment.
 * All methods are atomic. More complex manipulations with item should be implemented with {@link stonering.entity.job.action.Action}s.
 * Does not takes or puts items to map or containers, this should be done by actions.
 *
 * Target functionality:
 *  Creature have slots for wear with different layers of wears, slots for tools and other items(grab slots). 
 *  To equip some wear item or do any other manipulation with equipment, unit should has at least one grab slot free. 
 *  Unit hauls items in grab slots.
 *
 * MVP functionality:
 *  Creature have slots for wear without layers, slots for tools (grab slots) and special slot for hauling items.
 *  Interactions with items do not require free grab slots.
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
        if(equipment.equippedItems.contains(item)) 
            return Logger.ITEMS.logError("Equipping already equipped item " + item + " to unit " + equipment.entity, false);
        
        // item is tool
        if(item.type.tool != null) {
            GrabEquipmentSlot emptySlot = equipment.grabSlots.values().stream().filter(slot -> slot.grabbedItem == null).findFirst().orElse(null);
            if(emptySlot == null) return Logger.EQUIPMENT.logError("Equipping tool " + item + " to unit " + equipment  + " while no grab slots are free.", false);
            emptySlot.grabbedItem = item;
            return true;
        }
        
        //item is wear
        //TODO add layers check when layers will be added
        WearAspect wear = item.type.getAspect(WearAspect.class);
        if(wear != null) {
            EquipmentSlot slot = equipment.slots.get(wear.slot);
            if(slot.item != null) return Logger.EQUIPMENT.logError("Equipping item " + item + " into an occupied slot" + slot.name + " of unit " + equipment.entity ,false);
            slot.item = (Item) wear.entity; // equip item
            return true;
        }
        return Logger.ITEMS.logError("Attempt to equip item " + item + " as wear or tool to unit " + equipment.entity, false);
    }

    //TODO add layers
    public boolean freeSlot(EquipmentAspect equipment, String slotName) {
        EquipmentSlot slot = equipment.slots.get(slotName);
        if(slot == null) return Logger.EQUIPMENT.logError("Slot " + slotName + " not found on creature " + equipment.entity, false);
        if(slot.item == null) return Logger.EQUIPMENT.logError("Attempt to free slot " + slotName + ", which is already free", false);
        slot.item = null;
        return true;
    }
    
    /**
     * Equips item for hauling by unit. 
     * Disregarding item type it will not be equipped into any slot.
     */
    public boolean haulItem(Item item, EquipmentAspect equipment) {
        if(equipment.hauledItem != null) return Logger.EQUIPMENT.logError(equipment.entity + " attempts to haul " + item + " while hauling " + equipment.hauledItem, false);
        equipment.hauledItem = item;
        return true;
    }

    public boolean dropHauledItem(EquipmentAspect equipment) {
        if(equipment.hauledItem == null) return Logger.EQUIPMENT.logError(equipment.entity + " attempts to drop hauled item while not hauling any items.", false);
        return true;
    }
    
    /**
     * Finds appropriate slot for item. Item should be wear or tool.
     */
    public EquipmentSlot getSlotForEquipping(EquipmentAspect aspect, Item item) {
        aspect.slots.values().stream()
                .filter(slot -> slot.isSuitableFor(item))
                .max((slot1, slot2) -> {
            return slot1.
        })
        
        
        return aspect.slots.values().stream().filter(slot -> slot.isSuitableFor())
        WearAspect wear = item.getAspect(WearAspect.class);
        //TODO distinguish left and right items.
        if(wear != null && aspect.slots.get(wear.slot) != null) { // item is wear and slot exists 
            return aspect.slots.get(wear.slot);
        }
        if (item.isTool() && !aspect.grabSlots.isEmpty())
            return aspect.grabSlots.values().stream().filter(slot -> slot.isSuitableFor(item)).findFirst().orElse(aspect.grabSlots.get(0)); // item is tool and grab slot exists.
        return null;
    }

    /**
     * Finds slot for item and checks if some item is equipped there.
     */
    public Item getBlockingItem() {
        
    }
}
