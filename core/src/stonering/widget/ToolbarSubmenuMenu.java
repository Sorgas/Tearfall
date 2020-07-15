package stonering.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;

import stonering.enums.HotkeySequence;
import stonering.stage.toolbar.Toolbar;

import java.util.HashMap;
import java.util.List;

/**
 * Menu with buttons, which can show submenu in toolbar when pressed.
 * Holds map of submenus.
 * When submenu is shown, all other submenus are hidden.
 *
 * @author Alexander on 22.10.2018.
 */
public class ToolbarSubmenuMenu extends ToolbarButtonMenu {
    protected HashMap<String, ToolbarButtonMenu> submenus; // strings from item paths to submenus
    private HotkeySequence sequence;

    public ToolbarSubmenuMenu(Toolbar toolbar) {
        super(toolbar);
        submenus = new HashMap<>();
        sequence = new HotkeySequence();
    }

    public void addSubmenu(ToolbarButtonMenu menu, int hotkey, String buttonText, String iconName) {
        Actor thisMenu = this;
        addButton(buttonText, iconName, hotkey, () -> {
            toolbar.removeSubMenus(thisMenu);
            toolbar.addMenu(menu);
        });
        submenus.put(buttonText, menu);
    }
    
    /**
     * Tries to add button with given text and listener to the end of given submenu sequence.
     * Adds submenu or button to this menu.
     */
    public void addItem(String buttonText, String iconName, Runnable action, List<String> path) {
        if (path == null || path.isEmpty()) { //create button
            addButton(buttonText, iconName, sequence.getNext(), action);
        } else { // create submenu
            String currentStep = path.remove(0);
            if (!submenus.containsKey(currentStep)) { // no submenu for this step, create submenu
                addSubmenu(new ToolbarSubmenuMenu(toolbar), sequence.getNext(), currentStep, iconName); //TODO generalize
            }
            ((ToolbarSubmenuMenu) submenus.get(currentStep)).addItem(buttonText, iconName, action, path); // proceed to submenu with reduced path
        }
    }
}
