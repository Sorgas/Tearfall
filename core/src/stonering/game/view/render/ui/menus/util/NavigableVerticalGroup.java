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

/**
 * Vertical group which can handle input to change selected element.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup extends VerticalGroup implements Highlightable, HintedActor {
    public final Map<Integer, ControlActionsEnum> keyMapping; // additional keys to actions mapping.
    private EventListener selectListener;
    private EventListener cancelListener;
    private HighlightHandler highlightHandler;
    private int selectedIndex = -1;

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
                ControlActionsEnum action = keyMapping.get(keycode);
                if (action == null) action = ControlActionsEnum.getAction(keycode);
                event.stop();
                switch (action) {
                    case UP:
                        return navigate(-1);
                    case DOWN:
                        return navigate(1);
                    case SELECT:
                        return selectListener != null && selectListener.handle(event);
                    case CANCEL:
                        System.out.println("qqqqqqqqqqqqqqqqq");
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

    public void setCancelListener(EventListener cancelListener) {
        this.cancelListener = cancelListener;
    }

    public void setSelectListener(EventListener selectListener) {
        this.selectListener = selectListener;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    @Override
    public void setHighlightHandler(HighlightHandler handler) {
        highlightHandler = handler;
    }

    @Override
    public HighlightHandler getHighlightHandler() {
        return highlightHandler;
    }

    @Override
    public String getHint() {
        return "WS: navigate, ED: select";
    }


}
