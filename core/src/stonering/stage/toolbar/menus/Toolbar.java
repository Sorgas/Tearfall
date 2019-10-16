package stonering.stage.toolbar.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

/**
 * Contains table with all general orders menus.
 * TODO refactor event handling.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container<Table> {
    public final HorizontalGroup menusGroup; // in first row
    private Label status; // in second row
    private ParentMenu parentMenu; // always on the left end

    public Toolbar() {
        menusGroup = new HorizontalGroup();
        setFillParent(true);
        align(Align.bottomLeft);
        addListener(new InputListener() { // passes events to last menu in toolbar
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Actor target = menusGroup.getChildren().peek();
                Logger.UI.logDebug("handling " + keycode + " in toolbar. Target menu is " + target.getClass().getSimpleName());
                if (keycode == Input.Keys.E && target == parentMenu) return false;
                return target.notify(event, false);
            }
        });
        setActor(createToolbarTable());
        parentMenu = new ParentMenu(this);
        parentMenu.show();
        parentMenu.init();
    }

    /**
     * Creates menus row and status row.
     */
    private Table createToolbarTable() {
        Table table = new Table(StaticSkin.getSkin());
        menusGroup.align(Align.bottomRight);
        menusGroup.rowAlign(Align.bottom);
        table.add(menusGroup).row();
        table.add(status = new Label("", StaticSkin.getSkin())).left();
        return table;
    }

    public void addMenu(Actor menu) {
        menusGroup.addActor(menu);
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " added to toolbar");
    }

    /**
     * Removes given menu and all actors to the left.
     * Should be called before adding any other actors to toolbar.
     */
    public void hideMenu(Actor menu) {
        while (menusGroup.getChildren().contains(menu, true)) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " removed from toolbar");
    }

    /**
     * Removes all actors to the right from given menu, so it becomes last one.
     */
    public void hideSubMenus(Actor menu) {
        while (menusGroup.getChildren().contains(menu, true) && menusGroup.getChildren().peek() != menu) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        Logger.UI.logDebug("Submenus of " + menu.getClass().getSimpleName() + " removed from toolbar");
    }

    public void setText(String text) {
        Logger.UI.logDebug("Toolbar text set to " + text);
        status.setText(text);
    }
}
