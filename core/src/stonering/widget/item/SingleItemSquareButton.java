package stonering.widget.item;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import stonering.entity.item.Item;
import stonering.entity.RenderAspect;
import stonering.util.global.StaticSkin;

/**
 * Button that shows single item.
 *
 * @author Alexander on 29.01.2020
 */
public class SingleItemSquareButton extends Button {
    public static final int SHADING_COLOR = new Color(0.5f, 0.5f, 0.5f, 0.5f).toIntBits();
    public static final Texture SHADING_TEXTURE;
    public static final int SIZE = 64;
    
    private Item item;
    
    public final Stack stack;
    public final Image backgroundImage;
    public final Image itemImage;
    public final Image shadingImage;
    
    static { // TODO move static util objects to some static enum
        Pixmap map = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        map.drawPixel(0, 0, SHADING_COLOR);
        SHADING_TEXTURE = new Texture(map);
    }
    
    public SingleItemSquareButton(Item item, Drawable background) {
        super(StaticSkin.getSkin());
        this.add(stack = new Stack(
                wrapWithContainer(backgroundImage = new Image(background), SIZE), // TODO use another background
                wrapWithContainer(itemImage = new Image(), (int) (SIZE * 0.8f)), // item icon
                wrapWithContainer(shadingImage = new Image(SHADING_TEXTURE), SIZE))); // foreground
        shadingImage.setVisible(false);
        setItem(item);
    }
    
    protected Container<Actor> wrapWithContainer(Actor actor, int size) {
        return wrapWithContainer(actor).size(size, size);
    }

    protected Container<Actor> wrapWithContainer(Actor actor) {
        return new Container<>(actor);
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
