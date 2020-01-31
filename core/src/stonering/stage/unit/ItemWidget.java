package stonering.stage.unit;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.sun.istack.NotNull;
import stonering.entity.item.Item;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.entity.unit.aspects.equipment.EquipmentSlot;
import stonering.enums.images.DrawableMap;

/**
 * Square widget, that shows icon of item.
 * Is a stack of two images. Background image is larger than an icon image. Icon is wrapped in {@link Container} for aligning.
 *
 * @author Alexander on 29.01.2020
 */
public class ItemWidget extends Stack {
    private static final int ICON_SIZE = 64;
    private Image background;
    private Image itemIcon;

    public ItemWidget(Item item) {
        Drawable drawable = DrawableMap.getTextureDrawable("ui/item_slot.png");
        drawable.setMinWidth(80);
        drawable.setMinHeight(80);
        addActor(background = new Image(drawable));
        if(item != null) setIcon(item);
    }

    public ItemWidget(EquipmentSlot slot) {
        this(slot != null ? slot.item : null);
    }

    private void setIcon(@NotNull Item item) {
        addActor(new Container<>(itemIcon = new Image(item.getAspect(RenderAspect.class).region))); // for centering icon
        itemIcon.setSize(ICON_SIZE, ICON_SIZE);
    }
}
