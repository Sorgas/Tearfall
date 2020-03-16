package stonering.stage.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import stonering.enums.images.DrawableMap;
import stonering.stage.toolbar.ToolbarStage;
import stonering.stage.toolbar.menus.ParentMenu;
import stonering.util.global.Logger;
import stonering.util.global.StaticSkin;
import stonering.widget.ButtonMenu;

/**
 * Contains table with all general orders menus. Toolbar is focused on {@link ToolbarStage} and passes key presses to last(right) menu.
 * Most menus add its sub-menus into toolbar. Sub-menus are always displayed to the right from their parents.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container<Table> {
    public HorizontalGroup menusGroup; // in first row
    public Label status; // in second row
    public final ParentMenu parentMenu; // always on the left end
    public boolean enabled = true;

    public Toolbar() {
        createLayout();
        addMenu(parentMenu = new ParentMenu(this));
        addListener(new InputListener() { // passes events to last menu in toolbar
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Logger.UI.logDebug("handling " + keycode + " in toolbar");
                return menusGroup.getChildren().peek().notify(event, false);
            }
        });
    }

    private void createLayout() {
        setFillParent(true);
        Table table = new Table();
        align(Align.bottomLeft);
        table.add(menusGroup = new HorizontalGroup()).row();
        menusGroup.align(Align.bottomLeft);
        menusGroup.rowAlign(Align.bottom);
        table.add(status = new Label("", StaticSkin.getSkin())).left();
        setActor(table);
    }

    public void addMenu(ButtonMenu menu) {
        menusGroup.addActor(menu);
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " added to toolbar");
    }

    public void removeMenu(Actor menu) {
        while (menusGroup.getChildren().contains(menu, true)) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " removed from toolbar");
    }

    public void removeSubMenus(Actor menu) {
        while (menusGroup.getChildren().contains(menu, true) && menusGroup.getChildren().peek() != menu) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        Logger.UI.logDebug("Submenus of " + menu.getClass().getSimpleName() + " removed from toolbar");
    }

    public void setEnabled(boolean status) {
        enabled = status;
        if(enabled) {
            for (Actor child : menusGroup.getChildren()) {
                ((ButtonMenu) child).setBackground(DrawableMap.REGION.getDrawable("default:focused"));
            }
        } else {

        }
    }
}
