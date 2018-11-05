package stonering.game.core.view.render.ui.components.menus.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.HotkeySequence;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.building.BuildingCategoryMenu;

import java.util.HashMap;
import java.util.List;

/**
 * Menu with buttons, which show submenu in toolbar when pressed.
 *
 * @author Alexander on 22.10.2018.
 */
public class SubMenuMenu extends ButtonMenu {
    protected HashMap<String, SubMenuMenu> menus;
    private HotkeySequence sequence;

    public SubMenuMenu(GameMvc gameMvc) {
        super(gameMvc, true);
        menus = new HashMap<>();
        sequence = new HotkeySequence();
    }

    /**
     * Builds menu widget and inits child menus.
     */
    @Override
    public void init() {
        menus.values().forEach(SubMenuMenu::init);
        super.init();
    }

    /**
     * Creates button, submenu, and links them via button listener.
     *
     * @param menu
     * @param hotkey
     * @param identifier
     */
    public void addMenu(SubMenuMenu menu, int hotkey, String identifier) {
        Actor thisMenu = this;
        createButton(identifier, hotkey, new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toolbar.hideSubMenus(thisMenu);
                menu.show();
            }
        }, true);
        menus.put(identifier, menu);
    }

    /**
     * Tries to add button with given text and listener to the end of geven submenu sequence.
     * Add submenu or button to this menu.
     */
    public void addItem(String lastButtonText, ChangeListener listener, List<String> path) {
        if (!path.isEmpty()) { // create submenu
            String currentStep = path.remove(0);
            if (!menus.keySet().contains(currentStep)) { // create submenu
                addMenu(new BuildingCategoryMenu(gameMvc, currentStep), sequence.getNext(), currentStep);
            }
            menus.get(currentStep).addItem(lastButtonText, listener, path); // proceed to submenu with reduced path
        } else { //create button
            int hotkey = sequence.getNext();
            createButton(lastButtonText, hotkey, listener, true);
        }
    }

    @Override
    public void reset() {

    }
}
