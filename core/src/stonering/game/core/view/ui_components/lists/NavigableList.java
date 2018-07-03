package stonering.game.core.view.ui_components.lists;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import stonering.utils.global.StaticSkin;

/**
 * List that can handle navigation input.
 * @author Alexander on 03.07.2018.
 */
public abstract class NavigableList extends List {

    public NavigableList() {
        super(StaticSkin.getSkin());
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
