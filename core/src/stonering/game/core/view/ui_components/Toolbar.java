package stonering.game.core.view.ui_components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.view.ui_components.menus.BuildingMenu;
import stonering.game.core.view.ui_components.menus.DiggingMenu;
import stonering.game.core.view.ui_components.menus.Menu;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * Component of toolbar and its state
 *
 * Created by Alexander on 19.12.2017.
 */
public class Toolbar extends Menu {
    public static final String TOOLBAR = "bar";
    public static final String DIGGING = "digging";
    public static final String BUILDING = "building";

    private String activeMenu;
    private HashMap<String, Menu> menuMap;

    public Toolbar() {
        super();
        activeMenu = TOOLBAR;
        menuMap = new HashMap<>();
        menuMap.put(TOOLBAR, this);
        createTable();
    }

    private void createTable() {
        this.defaults().padLeft(10);
        this.pad(10);
        this.setFillParent(true);
        this.right().bottom();
        this.add(initMenu(new DiggingMenu(), "D: digging", 'd', DIGGING));
        this.add(initMenu(new BuildingMenu(), "B: building", 'b', BUILDING));
    }

    private Menu initMenu(Menu menu, String text, char hotkey, String mapKey) {
        menu.setVisible(false);
        menu.setToolbar(this);
        menuMap.put(mapKey, menu);
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMenu(mapKey);
            }
        });
        this.add(button).row();
        hotkeyMap.put(hotkey, button);
        return menu;
    }

    public boolean isMenuOpen(String menu) {
        return activeMenu.equals(menu);
    }

    public void toggleMenu(String menu) {
        activeMenu = activeMenu.equals(menu) ? TOOLBAR : menu;
        hideInactiveMenus();
    }

    private void hideInactiveMenus() {
        menuMap.forEach((key, menu) -> menuMap.get(key).setVisible(activeMenu.equals(key) || key.equals(TOOLBAR)));
    }

    public boolean handlePress(char c) {
        return menuMap.get(activeMenu).invokeByKey(c);
    }

    public Menu getMenu(String menu) {
        return menuMap.get(menu);
    }

    public void closeMenus() {
        toggleMenu(TOOLBAR);
    }
}
