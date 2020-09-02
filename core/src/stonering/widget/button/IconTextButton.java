package stonering.widget.button;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.sun.istack.Nullable;

import stonering.util.lang.StaticSkin;

/**
 * Button with icon, text. Icon is sized to be square.
 *
 * @author Alexander on 15.10.2019.
 */
public class IconTextButton extends Button {
    public static final int DEFAULT_ICON_SIZE = 50;
    public Cell<Image> imageCell;
    public Cell<Label> labelCell;
    public Stack stack;

    public IconTextButton(@Nullable Drawable drawable, @Nullable String text, int iconSize) {
        super(StaticSkin.getSkin());
        Button q = this;
        Image image = new Image(drawable);
        if (drawable != null) imageCell = add(image).size(iconSize);
        if (text != null) labelCell = add(new Label(text, StaticSkin.skin())).growX().left();
        setDebug(true, true);
    }

    public IconTextButton(@Nullable Drawable drawable, @Nullable String text) {
        this(drawable, text, DEFAULT_ICON_SIZE);
    }
}
