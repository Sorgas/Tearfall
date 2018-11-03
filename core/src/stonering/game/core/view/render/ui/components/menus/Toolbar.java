package stonering.game.core.view.render.ui.components.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

import java.util.ArrayList;

/**
 * Contains all general orders menus.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container implements Invokable {
    private GameMvc gameMvc;
    private Table toolbarTable; // in container
    private Table menusTable;   // in first row
    private Label status;       // in second row
    private ParentMenu parentMenu; // always on the right end

    private ArrayList<Actor> displayedMenus; // index increases from left to right

    public Toolbar(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        displayedMenus = new ArrayList<>();
    }

    public void init() {
        this.setFillParent(true);
        this.align(Align.bottomRight);
        this.setActor(createToolbarTable());
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
        parentMenu = new ParentMenu(gameMvc);
        parentMenu.init();
        parentMenu.show();
        refill();
        return menusTable;
    }

    private void refill() {
        menusTable.clearChildren();
        for (int i = 0; i < displayedMenus.size(); i++) {
            menusTable.add(displayedMenus.get(i));
        }
//        menusTable.add(parentMenu);
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
     * Removes given menu and all actors to the left.
     *
     * @param menu
     */
    public void hideMenu(Actor menu) {
        if (displayedMenus.contains(menu)) {
            while(displayedMenus.contains(menu)) {
                displayedMenus.remove(0);
            }
            System.out.println(menu.getClass().getSimpleName() + " hidden");
            refill();
        }
    }

    /**
     * Returns visible menu with lowest level (most left one).
     *
     * @return
     */
    public Invokable getActiveMenu() {
        for (int i = 0; i < menusTable.getChildren().size; i++) {
            if (Invokable.class.isAssignableFrom(menusTable.getChildren().get(i).getClass())) {
                return (Invokable) menusTable.getChildren().get(i);
            }
        }
        return null;
    }

    /**
     * Input entry point from {@link ToolBarController}.
     * Simply transfers event to current active menu.
     *
     * @param keycode pressed character.
     * @return true, if press handled
     */
    @Override
    public boolean invoke(int keycode) {
        return getActiveMenu().invoke(keycode);
    }

    public void setText(String text) {
        status.setText(text);
    }

    public void resetToLastMenu() {

    }
}
