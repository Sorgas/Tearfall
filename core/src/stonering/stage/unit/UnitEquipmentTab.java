package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;

/**
 * Group with several columns of {@link ItemWidget}.
 * Container with a {@link Stack} that shows icons of a unit's equipped items, and list of hauled items.
 * Each slot image is container in own container.
 *
 * @author Alexander on 28.01.2020.
 */
public class UnitEquipmentTab extends Container<HorizontalGroup> {
    private Table[] columns;
    private EquipmentAspect aspect;

    public UnitEquipmentTab(Unit unit) {
        aspect = unit.getAspect(EquipmentAspect.class);
        columns = new Table[5];
        HorizontalGroup group = new HorizontalGroup();
        setActor(group.rowAlign(Align.top));
        for (int i = 0; i < 5; i++) {
            group.addActor(columns[i] = new Table());
        }
        setSize(600, 800);
        fillSlots();
    }

    private void fillSlots() {
        addSlot(0, "right lower arm", 186);
        addSlot(0, "right second finger", 33);
        addSlot(0, "right hand", 33);
        addSlot(1, "right foot", 0);
        addSlot(2, "head", 0);
        addSlot(2, "neck", 33);
        addSlot(2, "body", 73);
        addSlot(2, "belt", 73);
        addSlot(2, "legs", 33);
        addSlot(3, "cloak", 73);
        addSlot(3, "backpack", 33);
        addSlot(3, "left foot", 33);
        addSlot(4, "left lower arm", 186);
        addSlot(4, "left second finger", 33);
        addSlot(4, "left hand", 33);
    }

    private void addSlot(int column, String slotName, int spacing) {
        columns[column].add(new ItemWidget(aspect.slots.get(slotName))).spaceTop(spacing).row();
    }
}
