package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.utils.global.StaticSkin;

/**
 * Component of toolbar.
 * All menu commands first dispatched here, and then passed active menu.
 * <p>
 * @author Alexander Kuzyakov on 19.12.2017.
 */
public class Toolbar extends SubMenuMenu {

    public Toolbar(GameMvc gameMvc) {
        super(gameMvc, 0);
        initTable();
        createMenus();
    }

    private void createMenus() {
        initMenu(new PlantsMenu(gameMvc), "P: plants", 'p');
        initMenu(new DiggingMenu(gameMvc), "D: digging", 'd');
        initMenu(new GeneralBuildingMenu(gameMvc), "B: building", 'b');
    }

    private void initTable() {
        this.align(Align.bottom);
    }

    private void initMenu(ButtonMenu menu, String text, char hotkey) {
        menus.put(hotkey, menu);
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menus.get(hotkey).show();
            }
        });
        buttons.put(hotkey, button);

    }

    public boolean handlePress(char c) {
        boolean handled = menuLevels.getActiveMenu().invokeByKey(c);
        if(!handled && c == (char) 27) {

        }
        return handled;
    }

    @Override
    public void reset() {

    }
}
