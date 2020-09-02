package stonering.stage.entity_menu.unit;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import stonering.entity.item.Item;
import stonering.entity.unit.aspects.equipment.EquipmentAspect;
import stonering.enums.images.DrawableMap;
import stonering.util.lang.StaticSkin;
import stonering.widget.item.SingleItemSquareButton;

/**
 * Widget for drawing unit's slots content.
 *
 * @author Alexander on 6/28/2020
 */
public class SlotsWidget extends Table {
    private final EquipmentAspect aspect;
    public final List<Item> displayedItems;

    public SlotsWidget(EquipmentAspect aspect) {
        this.aspect = aspect;
        displayedItems = new ArrayList<>();
        fillSlots();
    }

    private void fillSlots() {
        createSlotRow("head");
        createSlotRow("body");
        createSlotRow("legs");
        createGrabSlotRow("right hand");
        createGrabSlotRow("left hand");
        createSlotRow("right foot");
        createSlotRow("left foot");
    }
    private void createSlotRow(String slotName) {
        Optional.ofNullable(aspect.slots.get(slotName))
                .ifPresent(slot -> {
                    add(new Label(slotName, StaticSkin.skin())).width(216).padLeft(20).colspan(2);
                    add(createButtonAndRegisterItem(slot.item)).row();
                });
    }

    private void createGrabSlotRow(String slotName) {
        Optional.ofNullable(aspect.grabSlots.get(slotName))
                .ifPresent(slot -> {
                    add(new Label(slotName, StaticSkin.skin())).width(152).padLeft(20);
                    add(createButtonAndRegisterItem(slot.grabbedItem));
                    add(createButtonAndRegisterItem(slot.item)).row();
                });
    }

    private SingleItemSquareButton createButtonAndRegisterItem(Item item) {
        displayedItems.add(item);
        return new SingleItemSquareButton(item, DrawableMap.getTextureDrawable("ui/item_slot.png"));
    }
}
