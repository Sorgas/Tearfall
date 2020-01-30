package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Group with several columns of {@link ItemWidget}.
 * Container with a {@link Stack} that shows icons of a unit's equipped items, and list of hauled items.
 * Each slot image is container in own container.
 *
 * @author Alexander on 28.01.2020.
 */
public class UnitEquipmentTab extends Container<HorizontalGroup> {
    private static Map<Position, String> slotMap; // holds positions inside tab for different slot types. z is used as padding to previous widget.
    private HorizontalGroup group;
    private Table[] columns;

    {
        slotMap = new HashMap<>();
        slotMap.put(new Position(0, 0, 186), "right lower arm");
        slotMap.put(new Position(0, 2, 33), "right second finger");
        slotMap.put(new Position(0, 3, 33), "right hand");
        slotMap.put(new Position(1, 1, 0), "right foot");
        slotMap.put(new Position(2, 0, 0), "head");
        slotMap.put(new Position(2, 1, 33), "neck");
        slotMap.put(new Position(2, 2, 73), "body");
        slotMap.put(new Position(2, 3, 73), "belt");
        slotMap.put(new Position(2, 4, 33), "legs");
        slotMap.put(new Position(3, 0, 73), "cloak");
        slotMap.put(new Position(3, 1, 33), "backpack");
        slotMap.put(new Position(3, 2, 33), "left foot");
        slotMap.put(new Position(4, 0, 186), "left lower arm");
        slotMap.put(new Position(4, 0, 33), "left second finger");
        slotMap.put(new Position(4, 0, 33), "left hand");
    }

    public UnitEquipmentTab(Unit unit) {
        setActor(group = new HorizontalGroup());
        columns = new Table[5];
        EquipmentAspect equipmentAspect = unit.getAspect(EquipmentAspect.class);
        group.rowAlign(Align.top);
        for (int i = 0; i < 5; i++) {
            columns[i] = new Table();
            group.addActor(columns[i]);
        }
        fillColumns(equipmentAspect);
        setSize(600, 800);
    }

    private void fillColumns(EquipmentAspect aspect) {
        slotMap.keySet().forEach(position -> {
            String slotName = slotMap.get(position);
            EquipmentSlot slot = aspect.slots.get(slotName);
            ItemWidget widget = new ItemWidget(slot != null ? slot.item : null);
            columns[position.x].add(widget).pad(8).spaceTop(position.z).row();
            if (!aspect.grabSlots.containsKey(slotName)) return;
            widget =  new ItemWidget(slot != null ? slot.item : null);
            columns[position.x].add(widget).space(8).spaceTop(position.z).row();
        });
        columns[0].add();
    }
}
