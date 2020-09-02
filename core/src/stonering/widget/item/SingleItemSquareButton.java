package stonering.widget.item;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import stonering.entity.item.Item;
import stonering.entity.RenderAspect;
import stonering.util.lang.StaticSkin;

/**
 * Button that shows single item. Can be set empty.
 * TODO create custom skin.
 *
 * @author Alexander on 29.01.2020
 */
public class SingleItemSquareButton extends Button {
    public static final int SIZE = 64;

    private Item item;
    
    public final Stack stack;
    public final Image backgroundImage;
    public final Image itemImage;
    public final Image shadingImage;

    public SingleItemSquareButton(Item item, Drawable background, int size) {
        super(StaticSkin.skin());
        getStyle().up = null;
        getStyle().down = null;
        getStyle().checked = null;
        this.add(stack = new Stack(
                wrapWithContainer(backgroundImage = new Image(background), size), // TODO use another background
                wrapWithContainer(itemImage = new Image(), (int) (size * 0.8f)), // item icon
                wrapWithContainer(shadingImage = new Image(StaticSkin.getColorDrawable(StaticSkin.buttonShade)), size))); // foreground
        shadingImage.setVisible(false);
        setItem(item);
    }

    public SingleItemSquareButton(Item item, Drawable background) {
        this(item, background, SIZE);
    }

    protected <T extends Actor> Container<T> wrapWithContainer(T actor, int size) {
        return new Container<>(actor).size(size, size);
    }

    @Override
    public void setDisabled(boolean isDisabled) {
        super.setDisabled(isDisabled);
        shadingImage.setVisible(isDisabled);
    }

    public void setItem(Item item) {
        this.item = item;
        if(item != null) itemImage.setDrawable(new TextureRegionDrawable(item.get(RenderAspect.class).region));
    }

    public Item getItem() {
        return item;
    }
}
