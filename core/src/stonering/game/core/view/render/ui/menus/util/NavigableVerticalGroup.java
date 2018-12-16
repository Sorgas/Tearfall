package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.utils.global.TagLoggersEnum;

import java.util.HashSet;
import java.util.Set;


/**
 * Vertical group which can handle input.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup extends VerticalGroup implements HideableComponent {
    private Set<Integer> selectKeys;
    private EventListener selectListener;
    private EventListener cancelListener;
    private EventListener showListener;
    private EventListener hideListener;

    private int selectedIndex = -1;

    public NavigableVerticalGroup() {
        super();
        createDefaultListener();
        selectKeys = new HashSet<>();
        selectKeys.add(Input.Keys.E);
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on NavigableVerticalGroup");
                if(selectKeys.contains(keycode)) {
                    select(event);
                    return true;
                }
                switch (keycode) {
                    case Input.Keys.W:
                        up();
                        return true;
                    case Input.Keys.S:
                        down();
                        return true;
                    case Input.Keys.Q:
                        cancel(event);
                        return true;
                    case Input.Keys.A:
                    case Input.Keys.D:
                    case Input.Keys.R:
                    case Input.Keys.F:
                        return true;         // to prevent camera movement
                }
                return false;
            }
        });
    }

    public void navigate(int delta) {

    }

    public void up() {
        if (selectedIndex > 0) {
            selectedIndex -= 1;
        }
    }

    public void down() {
        if (selectedIndex < getChildren().size - 1) {
            selectedIndex++;
        }
    }

    public void select(InputEvent event) {
        if (selectListener != null) {
            selectListener.handle(event);
        }
    }

    public void cancel(InputEvent event) {
        if (cancelListener != null) {
            cancelListener.handle(event);
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

    public Actor getSelectedElement() {
        if (selectedIndex >= 0) {
            return getChildren().get(selectedIndex);
        }
        return null;
    }

    public void setCancelListener(EventListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setSelectListener(EventListener selectListener) {
        this.selectListener = selectListener;
    }

    public void setShowListener(EventListener showListener) {
        this.showListener = showListener;
    }

    public void setHideListener(EventListener hideListener) {
        this.hideListener = hideListener;
    }

    public Set<Integer> getSelectKeys() {
        return selectKeys;
    }
}
