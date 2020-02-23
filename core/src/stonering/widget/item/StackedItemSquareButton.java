package stonering.widget.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.type.ItemType;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.List;

/**
 * Square button of item.
 * Consists of background, item icon, foreground (becomes shaded when button is disabled), and label with number.
 * Can store list of particular items, for further usage.
 * <p>
 * TODO add tooltip
 *
 * @author Alexander on 17.02.2020.
 */
public class StackedItemSquareButton extends Button {
    public static final int SHADING_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.5f).toIntBits();
    public static final Texture SHADING_TEXTURE;
    public static final int SIZE = 80;
    public final Stack stack = new Stack();
    public final List<Item> items = new ArrayList<>();
    public final ItemType type;
    public int number;
    private final Image shadingImage;

    static {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.drawPixel(0, 0, SHADING_COLOR);
        SHADING_TEXTURE = new Texture(map);
    }

    public StackedItemSquareButton(List<Item> items) {
        this(items.get(0).type, items.size());
        this.items.addAll(items);
    }

    public StackedItemSquareButton(Item item) {
        this(item.type, 1);
        this.items.add(item);
    }

    private StackedItemSquareButton(ItemType type, int number) {
        super(StaticSkin.getSkin());
        this.type = type;
        this.number = number;
        this.add(stack);
        stack.add(wrapWithContainer(new Image(DrawableMap.getTextureDrawable("ui/item_slot.png")), SIZE)); // TODO use another background
        stack.add(wrapWithContainer(new Image(AtlasesEnum.items.getBlockTile(type.atlasXY)))); // item icon
        stack.add(wrapWithContainer(shadingImage = new Image(SHADING_TEXTURE), SIZE)); // foreground
        shadingImage.setVisible(false);
        if(number <= 1) return; // no label for single item
        Label label = new Label(String.valueOf(number), StaticSkin.getSkin());
        label.setFillParent(true);
        label.setAlignment(Align.bottomLeft);
        stack.add(label); // number label
    }

    private Container<Actor> wrapWithContainer(Actor actor, int size) {
        return wrapWithContainer(actor).size(size, size);
    }

    private Container<Actor> wrapWithContainer(Actor actor) {
        return new Container<>(actor);
    }
}
