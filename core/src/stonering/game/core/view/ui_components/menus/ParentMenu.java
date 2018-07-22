package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.Input;
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
public class ParentMenu extends SubMenuMenu {

    public ParentMenu(GameMvc gameMvc) {
        super(gameMvc, false);
        initTable();
        createMenus();
    }

    private void createMenus() {
        initMenu(new PlantsMenu(gameMvc), "P: plants", Input.Keys.P);
        initMenu(new DiggingMenu(gameMvc), "O: digging", Input.Keys.O);
        initMenu(new GeneralBuildingMenu(gameMvc), "I: building", Input.Keys.I);
    }

    private void initTable() {
        this.align(Align.bottom);
    }

    private void initMenu(ButtonMenu menu, String text, int hotkey) {
        menus.put(hotkey, menu);
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSubMenuByHotkey(hotkey);
            }
        });
        buttons.put(hotkey, button);
    }

    @Override
    public boolean invoke(int keycode) {
        return super.invoke(keycode);
    }

    @Override
    public void reset() {

    }
}
