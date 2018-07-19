package stonering.game.core.view.ui_components.lists;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import stonering.game.core.GameMvc;
import stonering.game.core.view.ui_components.menus.HideableComponent;
import stonering.game.core.view.ui_components.menus.Toolbar;
import stonering.utils.global.StaticSkin;

/**
 * List that has navigation methods to be navigated through user input
 * .
 * @author Alexander Kuzyakov on 03.07.2018.
 */
public abstract class NavigableList extends List implements HideableComponent {
    protected GameMvc gameMvc;
    protected Toolbar toolbar;
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

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    public abstract void select();
}
