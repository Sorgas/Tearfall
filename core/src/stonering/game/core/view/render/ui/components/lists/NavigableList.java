package stonering.game.core.view.render.ui.components.lists;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.components.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.components.menus.Toolbar;
import stonering.utils.global.StaticSkin;

/**
 * Extends {@link List} with navigation methods.
 *
 * @author Alexander Kuzyakov on 03.07.2018.
 */
public abstract class NavigableList extends List {
    protected GameMvc gameMvc;
    protected boolean hideable = false;

    public NavigableList(GameMvc gameMvc, boolean hideable) {
        super(StaticSkin.getSkin());
        this.gameMvc = gameMvc;
        this.hideable = hideable;
    }


    public void up() {
        if (getSelectedIndex() > 0) {
            setSelectedIndex(getSelectedIndex() - 1);
        }
    }

    public void down() {
        if (getSelectedIndex() < getItems().size - 1) {
            setSelectedIndex(getSelectedIndex() + 1);
        }
    }

    public abstract void select();
}
