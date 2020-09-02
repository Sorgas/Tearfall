package stonering.widget.lists;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.utils.Array;
import stonering.widget.Highlightable;
import stonering.util.logging.Logger;
import stonering.util.lang.StaticSkin;

/**
 * Extends {@link List} with navigation methods.
 * Can be invoked with default keys. Should be used for in-game lists to provide consistency.
 *
 * @author Alexander Kuzyakov on 03.07.2018.
 */
public class NavigableList<T> extends List<T> implements Highlightable {
    private EventListener hideListener;
    private EventListener selectListener;
    private EventListener showListener;
    private HighlightHandler highlightHandler;

    public NavigableList() {
        super(StaticSkin.getSkin());
        clearListeners();              // clears listener for Ctrl+A
        createDefaultListener();
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                Logger.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on NavigableList");
                switch (keycode) {
                    case Input.Keys.W:
                        up();
                        return true;
                    case Input.Keys.S:
                        down();
                        return true;
                    case Input.Keys.E:
                        select(event);
                        return true;
                    case Input.Keys.Q:
                        hide(event);
                        return true;
                }
                return false;
            }
        });
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

    public void select(Event event) {
        if (selectListener != null) {
            selectListener.handle(event);
        }
    }

    public void show() {
        if (showListener != null) {
            showListener.handle(null);
        }
    }

    public void hide(Event event) {
        if (hideListener != null) {
            hideListener.handle(event);
        }
    }

    /**
     * Should not be used in loop.
     */
    public void addItem(T item) {
        Array<T> items = getItems();
        items.add(item);
        setItems(items);
    }

    /**
     * Invokes handler if actor is focused.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        updateHighlighting(getStage().getKeyboardFocus() == this);
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

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }

    @Override
    public void setHighlightHandler(HighlightHandler handler) {
        highlightHandler = handler;
    }
}
