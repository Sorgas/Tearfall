package stonering.game.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.enums.ControlActionsEnum;
import stonering.util.global.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Vertical group which can handle input to change selected element.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup extends VerticalGroup implements Highlightable {
    public Map<Integer, ControlActionsEnum> keyMapping;
    private EventListener selectListener;
    private EventListener cancelListener;
    private boolean highlighted;
    private Consumer<Boolean> highlightHandler;
    private int selectedIndex = -1;


    public NavigableVerticalGroup() {
        super();
        keyMapping = new HashMap<>();
        createDefaultListener();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (getStage() == null || highlightHandler == null) return;
        // highlighted should be true if this group is focused.
        if ((getStage().getKeyboardFocus() == this) != highlighted) highlightHandler.accept(highlighted = !highlighted);
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                ControlActionsEnum action = keyMapping.get(keycode);
                if (action == null) action = ControlActionsEnum.getAction(keycode);
                Logger.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on NavigableVerticalGroup");
                event.stop();
                switch (action) {
                    case UP:
                        return navigate(-1);
                    case DOWN:
                        return navigate(1);
                    case SELECT:
                        select(event);
                        return true;
                    case CANCEL:
                        cancel(event);
                        return true;
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
        selectedIndex = size != 0 ? (selectedIndex + delta + size) % size : -1;
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

    public boolean select(InputEvent event) {
        return selectListener != null && selectListener.handle(event);
    }

    public boolean cancel(InputEvent event) {
        return cancelListener != null && cancelListener.handle(event);
    }

    public Actor getSelectedElement() {
        if (selectedIndex >= 0 && selectedIndex < getChildren().size) {
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

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    @Override
    public void setHighlightHandler(Consumer<Boolean> handler) {
        highlightHandler = handler;
    }
}
