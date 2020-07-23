package stonering.stage.toolbar;

import java.util.Optional;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.utils.Align;

import stonering.enums.images.DrawableMap;
import stonering.stage.toolbar.menus.ParentMenu;
import stonering.util.logging.Logger;
import stonering.widget.ButtonMenu;
import stonering.widget.util.KeyNotifierListener;

/**
 * Contains table with all general orders menus. Toolbar is focused on {@link ToolbarStage} and passes key presses to last(right) menu.
 * Most menus add its sub-menus into toolbar. Sub-menus are always displayed to the right from their parents.
 *
 * @author Alexander Kuzyakov on 17.06.2018.
 */
public class Toolbar extends Container<HorizontalGroup> {
    public HorizontalGroup menusGroup; // in first row
    public final ParentMenu parentMenu; // always on the left end
    public boolean enabled = true;

    public Toolbar() {
        super();
        createLayout();
        addMenu(parentMenu = new ParentMenu(this));
        addListener(new KeyNotifierListener(menusGroup.getChildren()::peek));
    }

    private void createLayout() {
        setActor(menusGroup = new HorizontalGroup());
        align(Align.bottomLeft).setFillParent(true);
        menusGroup.align(Align.bottomLeft);
        menusGroup.rowAlign(Align.bottom);
    }

    public void addMenu(ButtonMenu menu) {
        highlightLast(false);
        menusGroup.addActor(menu);
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " added to toolbar");
        highlightLast(true);
    }

    public void removeMenu(Actor menu) {
        highlightLast(false);
        while (menusGroup.getChildren().contains(menu, true)) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        Logger.UI.logDebug("Menu " + menu.getClass().getSimpleName() + " removed from toolbar");
        highlightLast(true);
    }

    public void removeSubMenus(Actor menu) {
        highlightLast(false);
        while (menusGroup.getChildren().contains(menu, true) && menusGroup.getChildren().peek() != menu) {
            menusGroup.removeActor(menusGroup.getChildren().peek());
        }
        Logger.UI.logDebug("Submenus of " + menu.getClass().getSimpleName() + " removed from toolbar");
        highlightLast(true);
    }

    public void setEnabled(boolean status) {
        enabled = status;
        if (enabled) {
            for (Actor child : menusGroup.getChildren()) {
                ((ButtonMenu) child).setBackground(DrawableMap.REGION.getDrawable("default:focused"));
            }
        } else {

        }
    }

    public void reset() {
        removeSubMenus(parentMenu);
    }

    private void highlightLast(boolean value) {
        if (menusGroup.getChildren().isEmpty()) return;
        ((ButtonMenu) menusGroup.getChildren().peek())
                .setBackground(DrawableMap.REGION.getDrawable("toolbar_menu" + (value ? ":focused" : "")));
    }

    public void removeLastMenu() {
        Optional.ofNullable(menusGroup.getChildren().peek())
                .filter(menu -> menu != parentMenu)
                .ifPresent(this::removeMenu);
    }
}
