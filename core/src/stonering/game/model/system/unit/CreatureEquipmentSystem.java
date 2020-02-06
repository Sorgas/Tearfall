package stonering.game.model.system.unit;

import stonering.entity.item.Item;
import stonering.entity.item.aspects.WearAspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
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
        
    }
    
    /**
     * Equips wear on body and tools into hands.
     * Does nothing if some validation fails.
     */
    public boolean equipItem(EquipmentAspect aspect, Item item) {
        if(aspect.equippedItems.contains(item)) {
            return Logger.ITEMS.logError("Equipping already equipped item " + item + " to unit " + aspect.entity, false);
        }
        EquipmentSlot slot = getSlotForEquipping(aspect, item);
        if(item.type.tool != null) {
            return true;
        }
        WearAspect wear = item.type.getAspect(WearAspect.class);
        if(wear != null) {
            return true;
        }
        Logger.ITEMS.logError("Attempt to equip item " + item + " as wear or tool to unit " + aspect.entity);
        
        if (slot == null || !slot.addItem(item)) {
            Logger.ITEMS.logDebug(aspect.entity + " failed to equip item " + item + " into slot " + slot);
            return false; // slot is full
        }
        aspect.equippedItems.add(item);
        Logger.ITEMS.logDebug("Item " + item + " equipped by " + aspect.entity + " into slot " + slot.name);
        return true;
        
    }

    /**
     * Equips item for hauling by unit. 
     * Disregarding item type it will not be equipped into any slot.
     */
    public void haulItem() {
        
    }

    /**
     * Unequips item from unit. 
     */
    public void dropItem() {
        
    }

    /**
     * Finds appropriate slot for item. Item should be wear or tool.
     */
    public EquipmentSlot getSlotForEquipping(EquipmentAspect aspect, Item item) {
        WearAspect wear = item.getAspect(WearAspect.class);
        //TODO distinguish left and right items.
        if(wear != null && aspect.slots.get(wear.slot) != null) { // item is wear and slot exists 
            return aspect.slots.get(wear.slot);
        }
        if (item.isTool() && !aspect.grabSlots.isEmpty())
            return aspect.grabSlots.values().stream().filter(slot -> slot.canEquip(item)).findFirst().orElse(aspect.grabSlots.get(0)); // item is tool and grab slot exists.
        return null;
    }

    /**
     * Finds slot for item and checks if some item is equipped there.
     */
    public Item getBlockingItem() {
        
    }
}
