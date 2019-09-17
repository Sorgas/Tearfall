package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.HotkeySequence;
import stonering.game.GameMvc;

import java.util.HashMap;
import java.util.List;

/**
 * Menu with buttons, which show submenu in toolbar when pressed.
 *
 * @author Alexander on 22.10.2018.
 */
public class SubMenuMenu extends ToolbarButtonMenu {
    protected HashMap<String, SubMenuMenu> menus;   // strings from item paths to submenus
    private HotkeySequence sequence;

    public SubMenuMenu() {
        menus = new HashMap<>();
        sequence = new HotkeySequence();
    }

    /**
     * Builds menu widget and inits child menus.
     */
    public void init() {
        menus.values().forEach(SubMenuMenu::init);
    }

    /**
     * Creates button, submenu, and links them via button listener.
     */
    public void addMenu(SubMenuMenu menu, int hotkey, String identifier) {
        Actor thisMenu = this;
        createButton(identifier, hotkey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.instance().getView().getUiDrawer().getToolbar().hideSubMenus(thisMenu);
                menu.show();
            }
        }, true);
        menus.put(identifier, menu);
    }

    /**
     * Tries to add button with given text and listener to the end of given submenu sequence.
     * Adds submenu or button to this menu .
     */
    public void addItem(String lastButtonText, ChangeListener listener, List<String> path) {
        if (path == null || path.isEmpty()) { //create button
            createButton(lastButtonText, sequence.getNext(), listener, true);
        } else { // create submenu
            String currentStep = path.remove(0);
            if (!menus.keySet().contains(currentStep)) {    // no submenu for this step, create submenu
                addMenu(new SubMenuMenu(), sequence.getNext(), currentStep); //TODO generalize
            }
            menus.get(currentStep).addItem(lastButtonText, listener, path); // proceed to submenu with reduced path
        }
    }
}
