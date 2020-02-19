package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.type.ItemType;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.global.StaticSkin;

/**
 * Square button of item.
 * Consists of background, item icon, foreground (becomes shaded when button is disabled), and label with number.
 * 
 * TODO add tooltip
 * @author Alexander on 17.02.2020.
 */
public class StackedItemSquareButton extends Button {
    public final Stack stack;
    public final ItemType type;
    public int number;

    public StackedItemSquareButton(ItemType type, int number) {
        super(StaticSkin.getSkin());
        this.type = type;
        this.number = number;
        stack = new Stack();
        stack.add(wrapWithContainer(new Image(DrawableMap.getTextureDrawable("ui/item_slot.png")), 40)); // TODO use another background
        Container<Image> imageContainer = new Container<>(new Image(AtlasesEnum.items.getBlockTile(type.atlasXY))); 
        stack.add(imageContainer);
        // add foreground
        Label label = new Label(String.valueOf(number), StaticSkin.getSkin());
        label.setFillParent(true);
        label.setAlignment(Align.bottomLeft);
        stack.add(label);
        // add number label
        this.add(stack);
    }
    
    private Container wrapWithContainer(Actor actor, int size) {
        return new Container<>(actor).size(size, size);
    }
}
