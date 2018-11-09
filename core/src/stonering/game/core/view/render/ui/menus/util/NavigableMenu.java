package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import stonering.game.core.GameMvc;

import java.util.ArrayList;

/**
 * Adds scrolling logic to menu.
 *
 * @author Alexander on 20.10.2018.
 */
public abstract class NavigableMenu extends ButtonMenu {
    private boolean enabled = true;
    private int selectedItem = -1;
    private ArrayList<Button> buttonsList;

    public NavigableMenu(GameMvc gameMvc, boolean hideable) {
        super(gameMvc, hideable);
    }

    @Override
    public boolean invoke(int keycode) {
        if (enabled) {
            if (keycode == Input.Keys.W) {
                scroll(-1);
                return true;
            }
            if (keycode == Input.Keys.S) {
                scroll(1);
                return true;
            }
        }
        return super.invoke(keycode);
    }

    @Override
    public void reset() {

    }

    private void scroll(int delta) {
        selectedItem += delta;
        ensureBounds();
    }

    private void ensureBounds() {
        selectedItem = selectedItem < 0 ? 0 :
                (selectedItem >= buttonsList.size() ? buttonsList.size() : selectedItem);
    }

    public void setNavigationEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
