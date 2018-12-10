package stonering.game.core.view.render.ui.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.utils.Array;
import stonering.game.core.view.render.ui.menus.util.HideableComponent;
import stonering.game.core.view.render.ui.menus.util.Invokable;
import stonering.utils.global.StaticSkin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * SelectBox, which can be observed with keys. Key set is configurable.
 * Each action should be specified with listener.
 *
 * @author Alexander on 27.11.2018.
 */
public class NavigableSelectBox<T> extends SelectBox<T> implements HideableComponent {
    private int upKey = Input.Keys.W;
    private int downKey = Input.Keys.S;
    private int selectKey = Input.Keys.E;
    private int cancelKey = Input.Keys.Q;

    private EventListener selectListener;
    private EventListener cancelListener;
    private EventListener navigationListener;
    private EventListener showListener;
    private EventListener hideListener;

    public NavigableSelectBox() {
        super(StaticSkin.getSkin());
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                if (keycode == upKey) {
                    navigate(event, -1);
                    showList();
                    return true;
                } else if (keycode == downKey) {
                    navigate(event, 1);
                    showList();
                    return true;
                } else if (keycode == selectKey) {
                    if(getList().isVisible()) {
                        select();
                        hideList();
                    } else {
                        navigate(event, 1);
                        showList();
                    }
                    return true;
                } else if (keycode == cancelKey) {
                    cancel();
                    hideList();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    public void navigate(InputEvent event, int delta) {
        int newIndex = (getSelectedIndex() + delta) % getItems().size;
        setSelectedIndex(newIndex);
        getList().setSelectedIndex(newIndex);
    }

    /**
     * For selecting item from list.
     */
    public void select() {
        if (selectListener != null) {
            selectListener.handle(null);
        }
    }

    public void cancel() {
        if (cancelListener != null) {
            cancelListener.handle(null);
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

    public void setCancelListener(EventListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setNavigationListener(EventListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public void setShowListener(EventListener showListener) {
        this.showListener = showListener;
    }

    public void setUpKey(int upKey) {
        this.upKey = upKey;
    }

    public void setDownKey(int downKey) {
        this.downKey = downKey;
    }

    public void setSelectKey(int selectKey) {
        this.selectKey = selectKey;
    }

    public void setCancelKey(int cancelKey) {
        this.cancelKey = cancelKey;
    }
}
