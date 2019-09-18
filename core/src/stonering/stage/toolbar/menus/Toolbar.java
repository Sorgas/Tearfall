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
import stonering.widget.Highlightable;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;

import java.util.*;

/**
 * Contains table with all general orders menus.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container<Table> implements Highlightable {
    public final HorizontalGroup menusGroup; // in first row
    private Label status; // in second row
    private ParentMenu parentMenu; // always on the right end
    private HighlightHandler handler;

    public Toolbar() {
        menusGroup = new HorizontalGroup();
    }

    public void init() {
        setFillParent(true);
        align(Align.bottomLeft);
        createListeners();
        setActor(createToolbarTable());
        parentMenu = new ParentMenu();
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
        table.add(status = new Label("", StaticSkin.getSkin())).right();
        return table;
    }

    /**
     * Levels counted from right to left, widget indexes is opposite.
     */
    public void addMenu(Actor menu) {
        menusGroup.addActor(menu);
        updateHighlighting(true);
    }

    /**
     * Removes given menu and all actors to the left.
     * Should be called before adding any other actors to tollbar.
     */
    public void hideMenu(Actor menu) {
        if (!menusGroup.getChildren().contains(menu, true)) return;
        menusGroup.getChildren().removeRange(menusGroup.getChildren().indexOf(menu, true), menusGroup.getChildren().size - 1);
    }

    /**
     * Removes all actors to the right from given menu.
     */
    public void hideSubMenus(Actor menu) {
        if (!menusGroup.getChildren().contains(menu, true) || menusGroup.getChildren().peek() == menu) return;
        menusGroup.getChildren().removeRange(menusGroup.getChildren().indexOf(menu, true) + 1, menusGroup.getChildren().size - 1);
    }

    private void createListeners() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Logger.UI.logDebug("handling " + keycode + " in toolbar");
                if (keycode == Input.Keys.E && menusGroup.getChildren().peek() == parentMenu) return false;
                return menusGroup.getChildren().peek().notify(event, false);
            }
        });
        handler = new HighlightHandler(this) {

            @Override
            public void handle(boolean value) {
                for (int i = 0; i < ((Toolbar) owner).menusGroup.getChildren().size; i++) {
                    Actor child = ((Toolbar) owner).menusGroup.getChildren().get(i);
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

    public void setText(String text) {
        status.setText(text);
    }
}
