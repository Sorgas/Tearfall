package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.images.DrawableMap;
import stonering.widget.item.SingleItemSquareButton;

/**
 * Group with several columns of {@link SingleItemSquareButton}.
 * Container with a {@link Stack} that shows icons of a unit's equipped items, and list of hauled items.
 * Each slot image is container in own container.
 *
 * @author Alexander on 28.01.2020.
 */
public class UnitEquipmentTab extends Table {
    private VerticalGroup[] columns;
    private HorizontalGroup footRow;
    private EquipmentAspect aspect;

    public UnitEquipmentTab(Unit unit) {
        aspect = unit.get(EquipmentAspect.class);
        columns = new VerticalGroup[3];
        for (int i = 0; i < 3; i++) {
            add(columns[i] = new VerticalGroup()).expand().fill();
        }
        row();
        add(footRow = new HorizontalGroup()).expandX().fill().colspan(3);
        setSize(600, 800);
        fillSlots();
    }

    private void fillSlots() {
        columns[0].addActor(getButtonForSlot("right hand"));
        columns[1].addActor(getButtonForSlot("head"));
        columns[1].addActor(getButtonForSlot("body"));
        columns[1].addActor(getButtonForSlot("legs"));
        columns[2].addActor(getButtonForSlot("left hand"));
        footRow.addActor(getButtonForSlot("right foot"));
        footRow.addActor(getButtonForSlot("left foot"));
    }
    
    private SingleItemSquareButton getButtonForSlot(String slotName) {
        return new SingleItemSquareButton(aspect.slots.get(slotName).item, DrawableMap.getTextureDrawable("ui/item_slot.png"));
    }
}
