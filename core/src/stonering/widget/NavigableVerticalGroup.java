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

import java.util.HashMap;
import java.util.Map;

/**
 * Vertical group which can handle input to change selected element.
 * Also has listeners for selecting element and closing list.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup extends VerticalGroup implements Highlightable {
    public final Map<Integer, ControlActionsEnum> keyMapping; // additional keys to actions mapping.
    private EventListener selectListener;
    private EventListener cancelListener;
    protected HighlightHandler highlightHandler;
    protected int selectedIndex = -1;
    public boolean active; // if false, events aren't handled

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
                if (!active) return true;
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

    /**
     * Tries to move given actor on specified delta positions.
     */
    public void moveItem(Actor actor, int delta) {
        int currentIndex = getChildren().indexOf(actor, true);
        if (currentIndex >= 0) {
            int size = getChildren().size; // size is never 0 here
            removeActor(actor, false);
            int newIndex = (currentIndex + (delta % size) + size) % size;
            addActorAt(newIndex, actor);
            getStage().setKeyboardFocus(actor);
        }
    }

    public Actor getSelectedElement() {
        if (selectedIndex < 0 || selectedIndex >= getChildren().size) return null;
        return getChildren().get(selectedIndex);
    }

    /**
     * Sets selected index to given. If child with this index not exists, sets to last child.
     */
    public void setSelectedIndex(int newIndex) {
        selectedIndex = MathUtil.toRange(newIndex, -1, getChildren().size - 1);
    }

    public void setCancelListener(EventListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setSelectListener(EventListener selectListener) {
        this.selectListener = selectListener;
    }

    @Override
    public void setHighlightHandler(HighlightHandler handler) {
        highlightHandler = handler;
    }

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
