package stonering.game.core.view.render.ui.menus.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import stonering.entity.local.crafting.ItemOrder;
import stonering.utils.global.TagLoggersEnum;

import java.util.HashSet;
import java.util.Set;


/**
 * Vertical group which can handle input to change selected element.
 *
 * @author Alexander
 */
public class NavigableVerticalGroup extends VerticalGroup implements HideableComponent, Highlightable {
    private Set<Integer> selectKeys;
    private Set<Integer> upKeys;
    private Set<Integer> downKeys;
    private Set<Integer> cancelKeys;

    private EventListener selectListener;
    private EventListener cancelListener;
    private EventListener showListener;
    private EventListener hideListener;
    private EventListener preNavigationListener;   // if exists and returns false, no navigation performed
    private EventListener navigationListener;      // if exists and returns false, navigation does not stop event handling

    private int selectedIndex = -1;

    public NavigableVerticalGroup(boolean useDefaultKeys) {
        super();
        createDefaultListener();
        selectKeys = new HashSet<>();
        upKeys = new HashSet<>();
        downKeys = new HashSet<>();
        cancelKeys = new HashSet<>();
        if (useDefaultKeys) {
            initDefaultKeys();
        }
    }

    public NavigableVerticalGroup() {
        this(true);
    }

    private void initDefaultKeys() {
        selectKeys.add(Input.Keys.E);
        selectKeys.add(Input.Keys.D);
        upKeys.add(Input.Keys.W);
        downKeys.add(Input.Keys.S);
        cancelKeys.add(Input.Keys.Q);
        cancelKeys.add(Input.Keys.A);
    }

    private void createDefaultListener() {
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                event.stop();
                TagLoggersEnum.UI.logDebug("handling " + Input.Keys.toString(keycode) + " on NavigableVerticalGroup");
                if (selectKeys.contains(keycode)) {
                    select(event);
                    return true;
                } else if (upKeys.contains(keycode)) {
                    return navigate(-1);
                } else if (downKeys.contains(keycode)) {
                    return navigate(1);
                } else if (cancelKeys.contains(keycode)) {
                    cancel(event);
                } else {
                    return false;
                }
                return true;
            }
        });
    }

    /**
     * Navigates through children. Navigation
     * @param delta
     * @return
     */
    public boolean navigate(int delta) {

        if (preNavigationListener == null || preNavigationListener.handle(null)) {
            if(delta != 0) {
                int size = getChildren().size;
                if (size != 0) {
                    int newIndex = ((selectedIndex + delta) % size);
                    newIndex += newIndex < 0 ? size : 0;
                    selectedIndex = newIndex;
                } else {
                    selectedIndex = -1;
                }
            }
        }
        return navigationListener == null || navigationListener.handle(null);
    }

    public void moveItem(Actor actor, int delta) {
        delta = (int) Math.signum(delta);
        int index = getChildren().indexOf(actor, true);
        if (index >= 0) {
            int newIndex = index + delta;
            if (newIndex >= 0) {
                removeActor(actor, false);
                addActorAt(newIndex, actor);
                getStage().setKeyboardFocus(actor);
            }
        }
    }

    public boolean select(InputEvent event) {
        return selectListener != null && selectListener.handle(event);
    }

    public boolean cancel(InputEvent event) {
        return cancelListener != null && cancelListener.handle(event);
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
        if (selectedIndex >= 0 && selectedIndex < getChildren().size) {
            return getChildren().get(selectedIndex);
        }
        return null;
    }

    /**
     * Tries to highlight selected child.
     * @param value
     */
    @Override
    public void setHighlighted(boolean value) {
        for (Actor child : getChildren()) {
            if(child != null && child instanceof Highlightable) {
                ((Highlightable) child).setHighlighted(getSelectedElement() == child && value); // highlight only selected
            }
        }
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

    public void setNavigationListener(EventListener navigationListener) {
        this.navigationListener = navigationListener;
    }

    public Set<Integer> getSelectKeys() {
        return selectKeys;
    }

    public void setSelectKeys(Set<Integer> selectKeys) {
        this.selectKeys = selectKeys;
    }

    public Set<Integer> getUpKeys() {
        return upKeys;
    }

    public void setUpKeys(Set<Integer> upKeys) {
        this.upKeys = upKeys;
    }

    public Set<Integer> getDownKeys() {
        return downKeys;
    }

    public void setDownKeys(Set<Integer> downKeys) {
        this.downKeys = downKeys;
    }

    public Set<Integer> getCancelKeys() {
        return cancelKeys;
    }

    public void setCancelKeys(Set<Integer> cancelKeys) {
        this.cancelKeys = cancelKeys;
    }

    public EventListener getPreNavigationListener() {
        return preNavigationListener;
    }

    public void setPreNavigationListener(EventListener preNavigationListener) {
        this.preNavigationListener = preNavigationListener;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
