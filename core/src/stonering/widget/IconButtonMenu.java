package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.enums.images.DrawableMap;
import stonering.widget.button.IconTextButton;

/**
 * @author Alexander on 15.07.2020.
 */
public class IconButtonMenu extends ButtonMenu {
    protected int iconSize;
    
    public IconButtonMenu(int iconSize) {
        super();
        this.iconSize = iconSize;
    }
    
    public void addButton(String text, String iconName, int hotKey, Runnable action) {
        registerButton(createButton(Input.Keys.toString(hotKey) + ": " + text, iconName, action), hotKey);
    }
    
    protected Button createButton(String text, String iconName, Runnable action) {
        Drawable icon = DrawableMap.ICON.getDrawable(iconName);
        IconTextButton button = new IconTextButton(icon, text);
        if (action != null) button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                action.run();
            }
        });
        return button;
    }
}
