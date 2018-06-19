package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.utils.global.StaticSkin;

/**
 * Component of toolbar.
 * All menu commands first dispatched here, and then passed active menu.
 * <p>
 * Created by Alexander on 19.12.2017.
 */
public class Toolbar extends SubMenuMenu {
    private Menu activeMenu;

    public Toolbar(GameMvc gameMvc) {
        super(gameMvc);
        initTable();
        createMenus();
    }

    public void init() {
        menus.values().forEach((menu) -> menu.init());
    }

    private void createMenus() {
        initMenu(new PlantsMenu(gameMvc), "P: plants", 'p');
        initMenu(new DiggingMenu(gameMvc), "D: digging", 'd');
        initMenu(new GeneralBuildingMenu(gameMvc), "B: building", 'b');
        activeMenu = this;
    }

    private void initTable() {
        this.pad(10);
        this.setFillParent(false);
        this.right().bottom();
    }

    private void initMenu(Menu menu, String text, char hotkey) {
        menus.put(hotkey, menu);
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menus.get(hotkey).show();
                activeMenu = menus.get(hotkey);
            }
        });
        buttons.put(hotkey, button);

    }

    public boolean handlePress(char c) {
        return activeMenu.invokeByKey(c);
    }
}
