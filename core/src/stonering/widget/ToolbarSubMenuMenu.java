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
    protected HashMap<String, ToolbarSubMenuMenu> menus;   // strings from item paths to submenus
    private HotkeySequence sequence;

    public ToolbarSubMenuMenu(Toolbar toolbar) {
        super(toolbar);
        menus = new HashMap<>();
        sequence = new HotkeySequence();
    }

    /**
     * Builds menu widget and inits child menus.
     */
    public void init() {
        menus.values().forEach(ToolbarSubMenuMenu::init);
    }

    /**
     * Creates button, submenu, and links them via button listener.
     */
    public void addMenu(ToolbarSubMenuMenu menu, int hotkey, String identifier) {
        Actor thisMenu = this;
        createButton(identifier, hotkey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.instance().getView().mainUiStage.toolbar.hideSubMenus(thisMenu);
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
                addMenu(new ToolbarSubMenuMenu(toolbar), sequence.getNext(), currentStep); //TODO generalize
            }
            menus.get(currentStep).addItem(lastButtonText, listener, path); // proceed to submenu with reduced path
        }
    }
}
