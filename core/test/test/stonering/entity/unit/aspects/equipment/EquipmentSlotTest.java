package test.stonering.entity.unit.aspects.equipment;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import stonering.entity.item.Item;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.items.ItemGenerator;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 10.09.2019.
 */
public class EquipmentSlotTest {
    private static CreatureGenerator creatureGenerator;
    private static ItemGenerator itemGenerator;

    @BeforeAll
    static void prepare() {
        creatureGenerator = new CreatureGenerator();
        itemGenerator = new ItemGenerator();
    }

    /**
     * Tests equipping tools into single slot.
     */
    @Test
    public void testEquipTool() {
        Unit unit = creatureGenerator.generateUnit(new Position(0,0,0), "human");
        EquipmentAspect aspect = unit.getAspect(EquipmentAspect.class);
        EquipmentSlot slot = aspect.slots.get("right hand");
        EquipmentSlot slot2 = aspect.slots.get("left hand");

        Item item = itemGenerator.generateItem("pickaxe", "iron", null);
        aspect.equipItem(item);
        assert(slot != null && slot2 != null);
        assert(slot.hasItem(item) || slot2.hasItem(item));
        assert(aspect.equippedItems.contains(item));
        assert(aspect.toolWithActionEquipped("dig"));

        Item item2 = itemGenerator.generateItem("axe", "iron", null);
        aspect.equipItem(item2);
        assert(slot.hasItem(item2) || slot2.hasItem(item2));
        assert(aspect.equippedItems.contains(item) && aspect.equippedItems.contains(item2));
        assert(aspect.toolWithActionEquipped("chop"));

        Item item3 = itemGenerator.generateItem("hoe", "iron", null);
        aspect.equipItem(item3); // not equipped, as both hands are not free
        assert(!slot.hasItem(item3) && !slot2.hasItem(item3));
        assert(!aspect.toolWithActionEquipped("hoe"));
    }
}
