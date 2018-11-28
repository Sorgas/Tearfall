package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.List;

/**
 * Vertical group which can handle input.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup<T extends Highlightable> extends VerticalGroup implements Invokable, HideableComponent {
    private List<T> actorList;

    private EventListener hideListener;
    private EventListener selectListener;
    private EventListener showListener;

    private int selectedIndex = -1;

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
        if (selectedIndex > 0) {
            selectedIndex -= 1;
        }
    }

    public void down() {
        if (selectedIndex < actorList.size() - 1) {
            selectedIndex++;
        }
    }

    public void select() {
        if (selectListener != null) {
            selectListener.handle(null);
        }
    }

    @Override
    public void show() {
        if (showListener != null) {
            showListener.handle(null);
        }
    }

    @Override
    public void hide() {
        if (hideListener != null) {
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
