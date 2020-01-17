package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.stage.toolbar.ToolbarStage;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;
import stonering.widget.HintedActor;

/**
 * Contains table with all general orders menus. Toolbar is focused on {@link ToolbarStage} and passes key presses to last(right) menu.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container<Table> {
    public final HorizontalGroup menusGroup = new HorizontalGroup(); // in first row
    public Label status; // in second row
    private ParentMenu parentMenu; // always on the left end

    public Toolbar() {
        layoutToolbar();
        parentMenu = new ParentMenu(this);
        parentMenu.show();
        parentMenu.init();
        addListener(new InputListener() { // passes events to last menu in toolbar
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Actor target = menusGroup.getChildren().peek();
                Logger.UI.logDebug("handling " + keycode + " in toolbar. Target menu is " + target.getClass().getSimpleName());
                return target.notify(event, false);
            }
        });
    }

    /**
     * Creates menus row and status row.
     */
    private void layoutToolbar() {
        setFillParent(true);
        align(Align.bottomLeft);
        Table table = new Table(StaticSkin.getSkin());
        menusGroup.align(Align.bottomRight);
        menusGroup.rowAlign(Align.bottom);
        table.add(menusGroup).row();
        table.add(status = new Label("", StaticSkin.getSkin())).left();
        setActor(table);
    }

    public void addMenu(Actor menu) {
        menusGroup.addActor(menu);
        setStatusFromActorHint(menu);
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " added to toolbar");
    }

    /**
     * Removes given menu and all actors to the right. Updates status.
     */
    public void hideMenu(Actor menu) {
        while (menusGroup.getChildren().contains(menu, true)) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        setStatusFromActorHint(menusGroup.getChildren().peek());
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " removed from toolbar");
    }

    /**
     * Removes all actors to the right from given menu, so it becomes last one. Updates status.
     */
    public void hideSubMenus(Actor menu) {
        while (menusGroup.getChildren().contains(menu, true) && menusGroup.getChildren().peek() != menu) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        setStatusFromActorHint(menusGroup.getChildren().peek());
        Logger.UI.logDebug("Submenus of " + menu.getClass().getSimpleName() + " removed from toolbar");
    }

    public void hideAllMenus() {
        menusGroup.clearChildren();
    }

    private void setStatusFromActorHint(Actor actor) {
        status.setText(actor instanceof HintedActor ? ((HintedActor) actor).getHint() : "");
    }
}
