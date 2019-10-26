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
import stonering.util.geometry.Position;

import javax.xml.ws.soap.Addressing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander on 10.09.2019.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EquipmentSlotTest {
    private Unit unit;
    private EquipmentAspect aspect;
    private GrabEquipmentSlot slot1;
    private GrabEquipmentSlot slot2;
    private List<Item> items;

    @BeforeAll
    void prepare() {
        createUnit();
        createItems();
    }

    private void createUnit() {
        unit = new Unit(new Position(), new CreatureType());
        aspect = new EquipmentAspect(unit);
        slot1 = new GrabEquipmentSlot("right hand", Collections.emptyList());
        slot2 = new GrabEquipmentSlot("left hand", Collections.emptyList());
        aspect.slots.put(slot1.name, slot1);
        aspect.slots.put(slot2.name, slot2);
        aspect.grabSlots.put(slot1.name, slot1);
        aspect.grabSlots.put(slot2.name, slot2);
        unit.addAspect(aspect);
    }

    private void createItems() {
        String[] actions = {"chop", "dig", "hoe"};
        items = new ArrayList<>();
        for (String action : actions) {
            RawItemType rawType = new RawItemType();
            rawType.tool = new ToolItemType();
            ToolItemType.ToolAction toolAction = new ToolItemType.ToolAction();
            toolAction.action = action;
            rawType.tool.getActions().add(toolAction);
            ItemType type = new ItemType(new RawItemType());
            items.add(new Item(new Position(), type));
        }
    }

    /**
     * Tests equipping tools into single slot.
     */
    @Test
    public void testEquipTool() {
        Item item = items.get(0);
        aspect.equipItem(item);
        assert (slot1.hasItem(item) || slot2.hasItem(item));
        assert (aspect.equippedItems.contains(item));
        assert (aspect.toolWithActionEquipped("dig"));

        item = items.get(1);
        aspect.equipItem(item);
        assert (slot1.hasItem(item) || slot2.hasItem(item));
        assert (aspect.equippedItems.contains(item) && aspect.equippedItems.contains(item));
        assert (aspect.toolWithActionEquipped("chop"));

        item = items.get(2);
        aspect.equipItem(item); // not equipped, as both hands are not free
        assert (!slot1.hasItem(item) && !slot2.hasItem(item));
        assert (!aspect.toolWithActionEquipped("hoe"));
    }
}
