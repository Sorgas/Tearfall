package stonering.widget.button;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;

import stonering.enums.images.DrawableMap;
import stonering.util.lang.StaticSkin;

/**
 * {@link IconTextButton} with a hotkey image and label added upon it. 
 * 
 * @author Alexander on 09.07.2020.
 */
public class IconTextHotkeyButton extends IconTextButton {
    
    public IconTextHotkeyButton(Drawable drawable, String text, String hotkey) {
        super(drawable, text);
//        if (hotkey != null) stack.add(createHotKey(hotkey));
    }

    public IconTextHotkeyButton(Drawable drawable, String text, int hotkey) {
        this(drawable, text, Input.Keys.toString(hotkey));
    }
    
    private Container<Container<Label>> createHotKey(String hotkey) {
        Label label = new Label(hotkey, StaticSkin.skin()); // TODO create style for hotkeys (yellow font or smth)
        Container<Label> inner = new Container<>(label);
        inner.setBackground(DrawableMap.REGION.getDrawable("hotkey"));
        return new Container<>(inner).align(Align.bottomLeft);
    }
}
