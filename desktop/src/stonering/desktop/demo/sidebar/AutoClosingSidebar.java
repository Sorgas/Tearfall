package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * {@link Sidebar} that is closed if not extended more than 50% and opened fully otherwise.
 * Has listener that is notified when sidebar is closed.
 *
 * @author Alexander on 07.01.2020
 */
public class AutoClosingSidebar<T extends Actor> extends CloseableSidebar<T> {
    private ActorGestureListener paneFlickScrollListener;
    public int moveSpeed = 10; // used for closing or opening

    public AutoClosingSidebar(T widget, int align, float hideRatio) {
        super(widget, align, hideRatio);
        paneFlickScrollListener = (ActorGestureListener) pane.getListeners().get(0);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!paneFlickScrollListener.getGestureDetector().isPanning()) autoSlide();
    }

    private void autoSlide() {
        float current = getScrollAmount();
        float toClose = closedScrollValue - current;
        float toOpen = openedScrollValue - current;
        current += Math.abs(moveSpeed) * Math.signum((Math.abs(toClose) < Math.abs(toOpen)) ? toClose : toOpen);
        current = MathUtils.clamp(current, Math.min(closedScrollValue, openedScrollValue), Math.max(closedScrollValue, openedScrollValue));
        if (vertical) {
            pane.setScrollY(current);
        } else {
            pane.setScrollX(current);
        }
    }

    @Override
    public Container<ScrollPane> align(int align) {
        return super.align(align);
    }
}
