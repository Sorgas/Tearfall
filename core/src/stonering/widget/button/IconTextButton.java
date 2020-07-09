package stonering.widget.button;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
        Button q = this;
        Image image = new Image(drawable);
        if (drawable != null) imageCell = add(image);
        if (text != null) labelCell = add(new Label(text, StaticSkin.skin())).growX().left();
        setSize(getPrefWidth(), getPrefHeight());
        imageCell.size(new Value() {
            @Override
            public float get(Actor context) {
                return q.getHeight();
            }
        });
        setDebug(true, true);
    }
    
    
}
