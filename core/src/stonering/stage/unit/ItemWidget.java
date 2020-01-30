package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.sun.istack.NotNull;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.images.DrawableMap;

/**
 * Square widget, that shows icon of item.
 *
 * @author Alexander on 29.01.2020
 */
public class ItemWidget extends Stack {
    private Image background;
    private Image itemIcon;

    public ItemWidget(Item item) {
        addActor(background = new Image(DrawableMap.TEXTURE.getDrawable("ui/item_slot.png")));
        if(item != null) setIcon(item);
        setSize(64, 64);
    }

    private void setIcon(@NotNull Item item) {
        addActor(itemIcon = new Image(item.getAspect(RenderAspect.class).region));
        itemIcon.setSize(64, 64);
    }
}
