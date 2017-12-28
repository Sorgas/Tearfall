package stonering.game.core.view.ui_components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.view.ui_components.menus.DiggingMenu;
import stonering.game.core.view.ui_components.menus.Menu;
import stonering.utils.global.StaticSkin;

import java.util.HashMap;

/**
 * Created by Alexander on 19.12.2017.
 * <p>
 * component of toolbar and its state
 */
public class Toolbar extends Menu {
    public static final String TOOLBAR = "bar";
    public static final String DIGGING = "digging";

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
        addDiggingMenu();
    }

    private void addDiggingMenu() {
        DiggingMenu diggingMenu = new DiggingMenu();
        diggingMenu.setVisible(false);
        diggingMenu.setToolbar(this);
        this.add(diggingMenu);
        this.row();
        menuMap.put(DIGGING, diggingMenu);

        TextButton diggingButton = new TextButton("D: digging", StaticSkin.getSkin());
        diggingButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMenu(DIGGING);
            }
        });
        this.add(diggingButton).row();
        hotkeyMap.put('d', diggingButton);
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
