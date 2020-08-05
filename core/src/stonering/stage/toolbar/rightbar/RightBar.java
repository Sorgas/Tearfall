package stonering.stage.toolbar.rightbar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

import stonering.enums.images.DrawableMap;
import stonering.game.GameMvc;
import stonering.widget.button.IconTextHotkeyButton;

/**
 * Vertical widget with buttons that shows settlement management menus.
 * 
 * @author Alexander on 06.07.2020.
 */
public class RightBar extends Container<Table> {
    private Table buttonTable;
    private Container<Actor> menu;
    private RightBarMenuEnum shownMenu;

    public RightBar() {
        createLayout();
        createButton(RightBarMenuEnum.UNITS_MENU);
        createButton(RightBarMenuEnum.MILITARY_MENU);
    }

    private void createButton(RightBarMenuEnum type) {
        IconTextHotkeyButton button = new IconTextHotkeyButton(DrawableMap.ICON.getDrawable(type.ICON_NAME), null, Input.Keys.P);
        button.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                GameMvc.view().toolbarStage.toolbar.reset();
                if (shownMenu != type) {
                    menu.setActor(type.SUPPLIER.get());
                    shownMenu = type;
                } else {
                    menu.setActor(null);
                    shownMenu = null;
                }
            }
        });
        buttonTable.add(button).row();
    }

    private void createLayout() {
        Table table = new Table();
        table.defaults().align(Align.bottomRight);
        table.add(menu = new Container<>());
        table.add(buttonTable = new Table());
        buttonTable.defaults().size(80, 80);
        align(Align.bottomRight);
        setActor(table);
        setFillParent(true);
    }
}
