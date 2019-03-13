package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.toolbar.Toolbar;
import stonering.util.global.StaticSkin;
import stonering.util.global.TagLoggersEnum;

import java.util.HashMap;

/**
 * Abstract screen for toolbar.
 * Holds mappings of hotkeys to buttons and can simulate presses.
 * Some buttons show submenus.
 * Menus don't have their controllers, all behavior logic is written in their buttons.
 * Input comes from parent ButtonMenu, through invoke method.
 * Keys sets of menus should not overlap.
 * <p>
 *
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public abstract class ToolbarButtonMenu extends ButtonMenu {
    protected Toolbar toolbar;

    private HashMap<Integer, Button> buttons;

    public ToolbarButtonMenu(boolean hideable) {
        super(hideable);
        this.bottom();
    }

    /**
     * Builds screen widget.
     */
    public void init() {
        super.init();
        toolbar = gameMvc.getView().getUiDrawer().getToolbar();
    }

    /**
     * Cancels all inputs, like selected tools.
     */
    public abstract void reset();

    @Override
    public void show() {
        toolbar.addMenu(this);
    }

    @Override
    public void hide() {
        toolbar.hideMenu(this);
    }
}