package test.stonering.entity.unit.aspects.equipment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.GrabEquipmentSlot;
import stonering.enums.items.type.ItemType;
import stonering.enums.items.type.ToolItemType;
import stonering.enums.items.type.raw.RawItemType;
import stonering.enums.unit.CreatureType;
import stonering.game.model.system.unit.CreatureEquipmentSystem;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tests equipping tools into hands of a creature.
 * @author Alexander on 10.09.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreatureEquipmentSystemTest {
    private Unit unit;
    private EquipmentAspect equipment;
    private GrabEquipmentSlot slot1;
    private GrabEquipmentSlot slot2;
    private CreatureEquipmentSystem system;
    private List<Item> items;

    @BeforeAll
    void prepare() {
        system = new CreatureEquipmentSystem();
        createUnit();
        createItems();

    }

    private void createUnit() {
        unit = new Unit(new Position(), new CreatureType());
        equipment = new EquipmentAspect(unit);
        slot1 = new GrabEquipmentSlot("right hand", Collections.emptyList());
        slot2 = new GrabEquipmentSlot("left hand", Collections.emptyList());
        equipment.slots.put(slot1.name, slot1);
        equipment.slots.put(slot2.name, slot2);
        equipment.grabSlots.put(slot1.name, slot1);
        equipment.grabSlots.put(slot2.name, slot2);
        unit.addAspect(equipment);
    }

    private void createItems() {
        String[] actions = {"dig", "chop", "hoe"};
        items = new ArrayList<>();
        for (String action : actions) {
            RawItemType rawType = new RawItemType();
            rawType.tool = new ToolItemType();
            ToolItemType.ToolAction toolAction = new ToolItemType.ToolAction();
            toolAction.action = action;
            rawType.tool.getActions().add(toolAction);
            ItemType type = new ItemType(rawType);
            items.add(new Item(new Position(), type));
        }
    }

    /**
     * Tests equipping tools into single slot.
     */
    @Test
    public void testEquipTool() {
        Item item = items.get(0);
        system.equipItem(equipment, item);
        assert (slot1.grabbedItem != null || slot2.grabbedItem != null);
        assert (equipment.equippedItems.contains(item));
        assert (equipment.toolWithActionEquipped("dig"));

        item = items.get(1);
        system.equipItem(equipment, item);
        assert (slot1.grabbedItem != null || slot2.grabbedItem != null);
        assert (equipment.equippedItems.contains(item) && equipment.equippedItems.contains(item));
        assert (equipment.toolWithActionEquipped("chop"));

        item = items.get(2);
        system.equipItem(equipment, item); // not equipped, as both hands are not free
        assert (slot1.grabbedItem != item && slot2.grabbedItem != item);
        assert (!equipment.toolWithActionEquipped("hoe"));
    }
}
