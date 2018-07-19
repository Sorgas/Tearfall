package stonering.game.core.view.ui_components.menus;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.game.core.GameMvc;

/**
 * Component which could be shown or hidden from {@link Toolbar}
 *
 * @author Alexander Kuzyakov
 */
public abstract class ToolbarComponent extends VerticalGroup implements Invokable {
    protected Toolbar toolbar;
    protected GameMvc gameMvc;
    protected boolean hideable = false;

    public ToolbarComponent(GameMvc gameMvc, boolean hideable) {
        this.gameMvc = gameMvc;
        this.hideable = hideable;
    }

    public void init() {
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    /**
     * Adds all buttons from map to table and adds table to Toolbar widget.
     */
    public void show() {
        System.out.println(this.getClass().toString() + " shown");
        toolbar.addMenu(this);
    }

    /**
     * Removes all buttons.
     */
    public void hide() {
        System.out.println(this.getClass().toString() + " hid");
        toolbar.hideMenu(this);
    }

    @Override
    public abstract boolean invoke(char c);
}
