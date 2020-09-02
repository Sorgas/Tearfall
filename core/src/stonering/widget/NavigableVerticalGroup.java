package stonering.widget;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.enums.ControlActionsEnum;
import stonering.util.logging.Logger;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Vertical group which can handle input to change selected element.
 * Navigation listener is called on pressing select key or clicking on selected element.
 * Also has listeners for selecting element and closing list.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup<T extends Actor> extends VerticalGroup implements Highlightable {
    public final Map<Integer, ControlActionsEnum> keyMapping; // additional keys to actions mapping.
    public Consumer<T> selectListener = actor -> {}; // called on selection confirmation
    public Consumer<T> navigationListener = actor -> {}; // called when selection changes
    public Runnable cancelListener = () -> {}; // called on Q
    public HighlightHandler highlightHandler;
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
        // changes selected element, or clicks
        addCaptureListener(new InputListener() {
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

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                Logger.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on NavigableVerticalGroup");
                event.stop();
                switch (keyMapping.getOrDefault(keycode, ControlActionsEnum.getAction(keycode))) {
                    case UP:
                        navigate(-1);
                        break;
                    case DOWN:
                        navigate(1);
                        break;
                    case SELECT:
                        selectListener.accept(getSelectedElement());
                        break;
                    case CANCEL:
                        cancelListener.run();
                        break;
                }
                return true;
            }
        });
    }

    public void navigate(int delta) {
        int size = getChildren().size;
        setSelectedIndex(size != 0 ? (selectedIndex + delta + size) % size : -1);
    }

    public T getChildAtIndex(int index) {
        return (T) getChildren().get(index);
    }

    /**
     * Sets selected index to given. If child with this index not exists, sets to last child.
     */
    public void setSelectedIndex(int index) {
        if (selectedIndex == index) return;
        selectedIndex = Math.min(Math.max(index, -1), getChildren().size - 1);
        selectListener.accept(getSelectedElement());
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
