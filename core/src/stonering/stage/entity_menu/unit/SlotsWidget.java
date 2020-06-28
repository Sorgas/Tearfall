package stonering.stage.entity_menu.unit;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.images.DrawableMap;
import stonering.util.global.StaticSkin;
import stonering.widget.item.SingleItemSquareButton;

/**
 * Widget for drawing unit's slots content.
 *
 * @author Alexander on 6/28/2020
 */
public class SlotsWidget extends Table {
    private final EquipmentAspect aspect;
    public final List<Item> displayedItems;
//    private VerticalGroup[] slotColumns;
//    private HorizontalGroup footRow;

    public SlotsWidget(EquipmentAspect aspect) {
        this.aspect = aspect;
        displayedItems = new ArrayList<>();
//        slotColumns = new VerticalGroup[3];
//        for (int i = 0; i < 3; i++) {
//            add(slotColumns[i] = new VerticalGroup()).expand().fill();
//        }
//        row();
//        add(footRow = new HorizontalGroup()).expandX().fill().colspan(3);
        fillSlots();
    }

    private void fillSlots() {
//        slotColumns[0].addActor(getButtonForSlot("right hand"));
//        slotColumns[1].addActor(getButtonForSlot("head"));
//        slotColumns[1].addActor(getButtonForSlot("body"));
//        slotColumns[1].addActor(getButtonForSlot("legs"));
//        slotColumns[2].addActor(getButtonForSlot("left hand"));
//        footRow.addActor(getButtonForSlot("right foot"));
//        footRow.addActor(getButtonForSlot("left foot"));
        createSlotRow("right hand");
        createSlotRow("head");
        createSlotRow("body");
        createSlotRow("legs");
        createSlotRow("left hand");
        createSlotRow("right foot");
        createSlotRow("left foot");
    }

    private SingleItemSquareButton getButtonForSlot(String slotName) {
        return new SingleItemSquareButton(aspect.slots.get(slotName).item, DrawableMap.getTextureDrawable("ui/item_slot.png"));
    }

    private void createSlotRow(String slotName) {
        add(new Label(slotName, StaticSkin.skin())).width(216).padLeft(20);
        Item item = aspect.slots.get(slotName).item;
        if(item != null) displayedItems.add(item);
        add(new SingleItemSquareButton(aspect.slots.get(slotName).item, DrawableMap.getTextureDrawable("ui/item_slot.png"))).row();
    }
}
