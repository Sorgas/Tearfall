package stonering.widget.lists;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import stonering.util.global.StaticSkin;

/**
 * Button with icon and text.
 * Icon or text can be null.
 *
 * @author Alexander on 15.10.2019.
 */
public class IconTextButton extends Button {
    public Cell<Image> imageCell;
    public Cell<Label> labelCell;

    public IconTextButton(Drawable drawable, String text) {
        super(StaticSkin.getSkin());
        if (drawable != null) imageCell = add(new Image(drawable));
        if (text != null) labelCell = add(text);
    }

    public IconTextButton(String text) {
        this(null, text);
    }

    public IconTextButton(Drawable drawable) {
        this(drawable, null);
    }
}
