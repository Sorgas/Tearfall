package stonering.widget.button;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.sun.istack.Nullable;

import stonering.util.global.StaticSkin;

/**
 * Button with icon, text. Icon is sized to be square.
 *
 * @author Alexander on 15.10.2019.
 */
public class IconTextButton extends Button {
    public Cell<Image> imageCell;
    public Cell<Label> labelCell;
    public Stack stack;

    public IconTextButton(@Nullable Drawable drawable, @Nullable String text) {
        super(StaticSkin.getSkin());
        add(stack = new Stack()).grow();
        Table innerTable = new Table();
        if (drawable != null) imageCell = innerTable.add(new Image(drawable)).size(Value.percentHeight(1f, this));
        if (text != null) labelCell = innerTable.add(new Label(text, StaticSkin.skin())).expandX().left();
        stack.add(innerTable);
        setDebug(true, true);
    }
}
