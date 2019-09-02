package stonering.game.view.render.ui.menus.toolbar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.game.view.render.ui.menus.util.Highlightable;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

import java.util.*;

/**
 * Contains table with all general orders menus.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container implements Highlightable {
    private Table menusTable;       // in first row
    private Label status;           // in second row
    private ParentMenu parentMenu;  // always on the right end
    private List<Actor> displayedMenus; // index increases from left to right
    private HighlightHandler handler;

    public Toolbar() {
        displayedMenus = new ArrayList<>();
        createHighlightHandler();
    }

    public void init() {
        setFillParent(true);
        align(Align.bottomRight);
        setActor(createToolbarTable());
        createInputListener();
    }

    /**
     * Creates menus row and status row.
     */
    private Table createToolbarTable() {
        Table table = new Table(StaticSkin.getSkin());
        table.add(createMenusTable()).row();
        table.add(status = new Label("", StaticSkin.getSkin())).right();
        return table;
    }

    private Table createMenusTable() {
        menusTable = new Table();
        menusTable.align(Align.bottomRight);
        menusTable.defaults().align(Align.bottom);
        parentMenu = new ParentMenu();
        parentMenu.init();
        parentMenu.show();
        refill();
        return menusTable;
    }

    /**
     * This is used, because tool bar is a table and cannot be modified after creation.
     * TODO refactor toolbar to horizontalGroup
     */
    private void refill() {
        menusTable.clearChildren();
        for (Actor displayedMenu : displayedMenus) {
            menusTable.add(displayedMenu);
        }
        updateHighlighting(true);
    }

    /**
     * Levels counted from right to left, widget indexes is opposite.
     */
    public void addMenu(Actor menu) {
        displayedMenus.add(0, menu);
        refill();
        updateHighlighting(true);
    }


    /**
     * Removes given screen and all actors to the left.
     * Should be called before adding any other actors to tollbar.
     */
    public void hideMenu(Actor menu) {
        if (displayedMenus.contains(menu)) {
            while (displayedMenus.contains(menu)) {
                displayedMenus.remove(0);
            }
            refill();
        }
    }

    /**
     * Removes all actors to the left from given screen.
     *
     * @param menu
     */
    public void hideSubMenus(Actor menu) {
        if (displayedMenus.contains(menu)) {
            while (displayedMenus.get(0) != menu) {
                displayedMenus.remove(0);
            }
            refill();
        }
    }

    /**
     * Returns visible menu with lowest level (most left one).
     */
    public Actor getActiveMenu() {
        return menusTable.getChildren().isEmpty() ? null : menusTable.getChildren().get(0);
    }

    /**
     * Simply transfers event to current active menu.
     *
     * @return true, if press handled
     */
    private void createInputListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Logger.UI.logDebug("handling " + keycode + " in toolbar");
                if (keycode == Input.Keys.E && getActiveMenu() == parentMenu) return false;
                return getActiveMenu().notify(event, false);
            }
        });
    }

    public void setText(String text) {
        status.setText(text);
    }

    private void createHighlightHandler() {
        handler = new HighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                for (int i = 0; i < ((Toolbar) owner).menusTable.getChildren().size; i++) {
                    Actor child = ((Toolbar) owner).menusTable.getChildren().get(i);
                    if (!(child instanceof Highlightable)) continue;
                    ((Highlightable) child).updateHighlighting(i == 0); // only first child is highlighted
                }
            }
        };
    }

    @Override
    public Highlightable.HighlightHandler getHighlightHandler() {
        return handler;
    }
}
