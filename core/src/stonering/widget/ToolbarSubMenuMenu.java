package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.HotkeySequence;
import stonering.game.GameMvc;
import stonering.stage.toolbar.menus.Toolbar;

import java.util.HashMap;
import java.util.List;

/**
 * Menu with buttons, which show submenu in toolbar when pressed.
 *
 * @author Alexander on 22.10.2018.
 */
public class ToolbarSubMenuMenu extends ToolbarButtonMenu {
    protected HashMap<String, ToolbarButtonMenu> menus;   // strings from item paths to submenus
    private HotkeySequence sequence;

    public ToolbarSubMenuMenu(Toolbar toolbar) {
        super(toolbar);
        menus = new HashMap<>();
        sequence = new HotkeySequence();
    }

    /**
     * Creates button, submenu, and links them via button listener.
     */
    public void addMenu(ToolbarButtonMenu menu, int hotkey, String identifier, String iconName) {
        Actor thisMenu = this;
        createButton(identifier, iconName, hotkey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().toolbarStage.toolbar.removeSubMenus(thisMenu);
                menu.show();
            }
        }, true);
        menus.put(identifier, menu);
    }

    /**
     * Tries to add button with given text and listener to the end of given submenu sequence.
     * Adds submenu or button to this menu.
     */
    public void addItem(String lastButtonText, String iconName, ChangeListener listener, List<String> path) {
        if (path == null || path.isEmpty()) { //create button
            createButton(lastButtonText, iconName, sequence.getNext(), listener, true);
        } else { // create submenu
            String currentStep = path.remove(0);
            if (!menus.keySet().contains(currentStep)) {    // no submenu for this step, create submenu
                addMenu(new ToolbarSubMenuMenu(toolbar), sequence.getNext(), currentStep, iconName); //TODO generalize
            }
            ((ToolbarSubMenuMenu) menus.get(currentStep)).addItem(lastButtonText, iconName, listener, path); // proceed to submenu with reduced path
        }
    }
}
