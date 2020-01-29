package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.images.DrawableMap;

/**
 * Square widget, that shows icon of equipped item.
 *
 * @author Alexander on 29.01.2020
 */
public class ItemSlotWidget extends Stack {

    public ItemSlotWidget(Item item) {
        addActor(new Image(DrawableMap.instance().getFileDrawable("ui/item_slot.png")));
        Image itemImage = item != null ? new Image(item.getAspect(RenderAspect.class).region) : new Image();
        itemImage.setSize(64, 64);
        addActor(itemImage);
        setSize(64, 64);
    }
}
