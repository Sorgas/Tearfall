package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import stonering.enums.items.Recipe;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Extends {@link List} with navigation methods.
 * Can be invoked with default keys. Should be used for in-game lists to provide consistency.
 *
 * @author Alexander Kuzyakov on 03.07.2018.
 */
public class NavigableList extends List implements Invokable, HideableComponent {
    private EventListener hideListener;
    private EventListener selectListener;
    private EventListener showListener;

    public NavigableList() {
        super(StaticSkin.getSkin());
    }

    @Override
    public boolean invoke(int keycode) {
        switch (keycode) {
            case Input.Keys.R:
                up();
                return true;
            case Input.Keys.F:
                down();
                return true;
            case Input.Keys.E:
                select();
                return true;
            case Input.Keys.Q:
                hide();
                return true;
        }
        return false;
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

    public void select() {
        if(selectListener != null) {
            selectListener.handle(null);
        }
    }

    @Override
    public void show() {
        if(showListener != null) {
            showListener.handle(null);
        }
    }

    @Override
    public void hide() {
        if(hideListener != null) {
            hideListener.handle(null);
        }
    }

    public void setHideListener(EventListener hideListener) {
        this.hideListener = hideListener;
    }

    public void setSelectListener(EventListener selectListener) {
        this.selectListener = selectListener;
    }

    public void setShowListener(EventListener showListener) {
        this.showListener = showListener;
    }
}
