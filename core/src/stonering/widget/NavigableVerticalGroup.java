package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.enums.ControlActionsEnum;
import stonering.util.global.Logger;
import stonering.util.math.MathUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Vertical group which can handle input to change selected element.
 * Also has listeners for selecting element and closing list.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup<T extends Actor> extends VerticalGroup implements Highlightable {
    public final Map<Integer, ControlActionsEnum> keyMapping; // additional keys to actions mapping.
    public EventListener selectListener;
    public EventListener cancelListener;
    protected HighlightHandler highlightHandler;
    public int selectedIndex = -1;

    public NavigableVerticalGroup() {
        super();
        keyMapping = new HashMap<>();
        createDefaultListener();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getStage() == null) return;
        updateHighlighting(getStage().getKeyboardFocus() == this);
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Logger.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on NavigableVerticalGroup");
                event.stop();
                switch (keyMapping.getOrDefault(keycode, ControlActionsEnum.getAction(keycode))) {
                    case UP:
                        return navigate(-1);
                    case DOWN:
                        return navigate(1);
                    case SELECT:
                        return selectListener != null && selectListener.handle(event);
                    case CANCEL:
                        return cancelListener != null && cancelListener.handle(event);
                }
                return true;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                for (Actor child : getChildren()) {
                    if (!child.isAscendantOf(hit(x, y, false))) continue;
                    setSelectedIndex(getChildren().indexOf(child, true));
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * Navigates through children.
     */
    public boolean navigate(int delta) {
        int size = getChildren().size;
        setSelectedIndex(size != 0 ? (selectedIndex + delta + size) % size : -1);
        return true;
    }

    public T getChildAtIndex(int index) {
        return (T) getChildren().get(index);
    }

    /**
     * Sets selected index to given. If child with this index not exists, sets to last child.
     */
    public void setSelectedIndex(int newIndex) {
        selectedIndex = MathUtil.toRange(newIndex, -1, getChildren().size - 1);
        if(selectListener != null) selectListener.handle(null);
    }

    @Override
    public void setHighlightHandler(HighlightHandler handler) {
        highlightHandler = handler;
    }

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }

    public T getSelectedElement() {
        if (selectedIndex < 0 || selectedIndex >= getChildren().size) return null;
        return getChildAtIndex(selectedIndex);
    }

    public void setSelectedElement(T element) {
        selectedIndex = getChildren().indexOf(element, true);
    }

    public List<T> getElements() {
        return Arrays.stream(getChildren().items).map(child -> (T) child).collect(Collectors.toList());
    }
}
