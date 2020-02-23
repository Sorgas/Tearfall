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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.type.ItemType;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.Arrays;
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
    public static final int SIZE = 40;

    public final List<Item> items = new ArrayList<>();
    private Image itemImage;
    private Image shadingImage;
    public Label numberLabel;

    static {
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.drawPixel(0, 0, SHADING_COLOR);
        SHADING_TEXTURE = new Texture(map);
    }

    public StackedItemSquareButton(Item item) {
        this(Arrays.asList(item));
    }

    public StackedItemSquareButton(List<Item> items) {
        super(StaticSkin.getSkin());
        createLayout();
        setItems(items);
    }

    private void createLayout() {
        this.add(new Stack(
                wrapWithContainer(new Image(DrawableMap.getTextureDrawable("ui/item_slot.png")), SIZE), // TODO use another background
                wrapWithContainer(itemImage = new Image()), // item icon
                wrapWithContainer(shadingImage = new Image(SHADING_TEXTURE), SIZE), // foreground
                numberLabel = new Label("", StaticSkin.getSkin())));
        shadingImage.setVisible(false);
        numberLabel.setFillParent(true);
        numberLabel.setAlignment(Align.bottomLeft);
    }

    public void setItems(List<Item> items) {
        this.items.addAll(items);
        itemImage.setDrawable(new TextureRegionDrawable(AtlasesEnum.items.getBlockTile(items.get(0).type.atlasXY)));
        updateLabel();
    }

    private Container<Actor> wrapWithContainer(Actor actor, int size) {
        return wrapWithContainer(actor).size(size, size);
    }

    private Container<Actor> wrapWithContainer(Actor actor) {
        return new Container<>(actor);
    }

    public void updateLabel() {
        numberLabel.setText(items.size());
    }
}
