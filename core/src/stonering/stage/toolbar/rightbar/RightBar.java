package stonering.stage.toolbar.rightbar;

import java.util.Arrays;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.enums.images.DrawableMap;
import stonering.game.GameMvc;
import stonering.widget.button.IconTextHotkeyButton;
import stonering.widget.util.KeyNotifierListener;

/**
 * Vertical widget with buttons that shows settlement management menus.
 *
 * @author Alexander on 06.07.2020.
 */
public class RightBar extends Container<Table> {
    private Table buttonTable;
    private Container<Actor> container;
    private RightBarMenuEnum shownMenu;

    public RightBar() {
        createLayout();
        Arrays.stream(RightBarMenuEnum.values()).forEach(this::createButton);
        createListeners();
    }

    private void createButton(RightBarMenuEnum type) {
        IconTextHotkeyButton button = new IconTextHotkeyButton(DrawableMap.ICON.getDrawable(type.ICON_NAME), null, Input.Keys.P);
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().toolbarStage.toolbar.reset();
                if (shownMenu != type) { // other menu is shown
                    container.setActor(type.SUPPLIER.get()); // replace
                    shownMenu = type;
                } else { // this menu is shown
                    hideContent();
                }
            }
        });
        buttonTable.add(button).row();
    }

    public void showEntityMenu(Table entityMenu) {
        container.setActor(entityMenu); // replace
        shownMenu = null;
    }

    private void createLayout() {
        Table table = new Table();
        table.defaults().align(Align.bottomRight);
        table.add(container = new Container<>());
        table.add(buttonTable = new Table());
        buttonTable.defaults().size(80, 80);
        align(Align.bottomRight);
        setActor(table);
        setFillParent(true);
    }

    private void createListeners() {
        addListener(new KeyNotifierListener(() -> container.getActor())); // pass input to shown menu
        addListener(new InputListener() { // close current content
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.Q && container.hasChildren()) {
                    hideContent();
                    return true;
                }
                return false;
            }
        });
    }

    public void hideContent() {
        container.setActor(null); // close
        shownMenu = null;
    }
}
