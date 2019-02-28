package stonering.game.core.view.render.ui.menus;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.ButtonMenu;
import stonering.util.global.StaticSkin;

import java.util.*;

/**
 * Contains all general orders menus.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container {
    private Table toolbarTable; // in container
    private Table menusTable;   // in first row
    private Label status;       // in second row
    private ParentMenu parentMenu; // always on the right end

    private List<Actor> displayedMenus; // index increases from left to right

    public Toolbar() {
        displayedMenus = new ArrayList<>();
    }

    public void init() {
        setFillParent(true);
        align(Align.bottomRight);
        setActor(createToolbarTable());
        createInputListener();
    }

    /**
     * Creates menus row and status row.
     *
     * @return
     */
    private Table createToolbarTable() {
        toolbarTable = new Table(StaticSkin.getSkin());
        toolbarTable.add(createMenusTable()).row();
        toolbarTable.add(status = new Label("", StaticSkin.getSkin())).right();
        return toolbarTable;
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

    private void refill() {
        menusTable.clearChildren();
        for (Actor displayedMenu : displayedMenus) {
            menusTable.add(displayedMenu);
        }
    }

    /**
     * Levels counted from right to left, widget indexes is opposite.
     *
     * @param menu
     */
    public void addMenu(Actor menu) {
        System.out.println(menu.getClass().getSimpleName() + " shown");
        displayedMenus.add(0, menu);
        refill();
    }


    /**
     * Removes given screen and all actors to the left.
     * Should be called before adding any other actors to tollbar.
     *
     * @param menu
     */
    public void hideMenu(Actor menu) {
        if (displayedMenus.contains(menu)) {
            while (displayedMenus.contains(menu)) {
                displayedMenus.remove(0);
            }
            System.out.println(menu.getClass().getSimpleName() + " hidden");
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
     * Removes all non screen elements (like place select or lists).
     */
    public void closeNonMenuActors() {
        while (!(displayedMenus.get(0) instanceof ButtonMenu)) {
            displayedMenus.remove(0);
        }
        refill();
    }

    /**
     * Returns visible screen with lowest level (most left one).
     *
     * @return
     */
    public Actor getActiveMenu() {
        for (int i = 0; i < menusTable.getChildren().size; i++) {
            return menusTable.getChildren().get(i);
        }
        return null;
    }

    /**
     * Simply transfers event to current active screen.
     *
     * @return true, if press handled
     */
    private void createInputListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.E && getActiveMenu() == parentMenu) {
                    return false;
                }
                return getActiveMenu().fire(event);
//                return true;
            }
        });
    }

    public void setText(String text) {
        status.setText(text);
    }
}
