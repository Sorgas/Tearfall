package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import stonering.game.core.view.ui_components.Toolbar;

import java.util.HashMap;

/**
 * Created by Alexander on 27.12.2017.
 *
 * holds mappings of hotkeys to buttons and can simulate presses
 */
public abstract class Menu extends Table {
    protected HashMap<Character,Button> hotkeyMap;
    protected Toolbar toolbar;

    public Menu() {
        this(null);
    }

    public Menu(Skin skin) {
        super(skin);
        hotkeyMap = new HashMap<>();
    }

    public boolean inSet(char c) {
        return hotkeyMap.keySet().contains(c);
    }

    public boolean invokeByKey(char c) {
        if(hotkeyMap.keySet().contains(c)) {
            hotkeyMap.get(c).toggle();
            return true;
        }
        return false;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
    }
}
