package stonering.game.core.view.ui_components;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.view.ui_components.menus.*;
import stonering.utils.global.StaticSkin;

/**
 * Component of toolbar and its state
 *
 * Created by Alexander on 19.12.2017.
 */
public class Toolbar extends SubMenuMenu {
    public static final String TOOLBAR = "bar";
    public static final String DIGGING = "digging";
    public static final String PLANTS = "plants";
    public static final String BUILDING = "building";

    private String activeMenu;

    public Toolbar() {
        super();
        activeMenu = TOOLBAR;
        menus.put(TOOLBAR, this);
        createTable();
    }

    private void createTable() {
        this.defaults().padLeft(10);
        this.pad(10);
        this.setFillParent(true);
        this.right().bottom();

        this.add(initMenu(new PlantsMenu(), "P: plants", 'p', PLANTS));
        this.add(initMenu(new DiggingMenu(), "D: digging", 'd', DIGGING));
        this.add(initMenu(new GeneralBuildingMenu(), "B: building", 'b', BUILDING));
    }

    private Menu initMenu(Menu menu, String text, char hotkey, String mapKey) {
        menu.setVisible(false);
        menu.setToolbar(this);
        menus.put(mapKey, menu);
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleMenu(mapKey);
            }
        });
        this.add(button).row();
        hotkeys.put(hotkey, button);
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
        menus.forEach((key, menu) -> menus.get(key).setVisible(activeMenu.equals(key) || key.equals(TOOLBAR)));
    }

    public boolean handlePress(char c) {
        return menus.get(activeMenu).invokeByKey(c);
    }

    public Menu getMenu(String menu) {
        return menus.get(menu);
    }

    public void closeMenus() {
        toggleMenu(TOOLBAR);
    }
}
