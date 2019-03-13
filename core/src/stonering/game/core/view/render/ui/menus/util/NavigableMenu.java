package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;

/**
 * Adds scrolling logic to screen.
 *
 * @author Alexander on 20.10.2018.
 */
public abstract class NavigableMenu extends ToolbarButtonMenu {
    private boolean enabled = true;
    private int selectedItem = -1;
    private ArrayList<Button> buttonsList;

    public NavigableMenu(boolean hideable) {
        super(hideable);
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " in NavigableMenu");
                event.stop();
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
                return false;
            }
        });
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
