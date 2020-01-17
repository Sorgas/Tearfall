package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.Align;

/**
 * {@link Sidebar} that is closed if not extended more than 50% and opened fully otherwise.
 * Has listener that is notified when sidebar is closed.
 *
 * @author Alexander on 07.01.2020
 */
public class AutoClosingSidebar<T extends Actor> extends Sidebar<T> {
    private ActorGestureListener paneFlickScrollListener;
    private float closedScrollValue;
    private float openedScrollValue;

    public int moveSpeed = 10; // used for closing or opening, should be positive
    private Runnable closedListener; // called when sidebar is closed
    private Runnable openedListener; // called when sidebar is opened
    private boolean closed;
    private boolean opened;

    public AutoClosingSidebar(T widget, int align, float hideRatio) {
        super(widget, align, hideRatio);
        paneFlickScrollListener = (ActorGestureListener) pane.getListeners().get(0);
        closedListener = () -> {};
        updateScrollBounds();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!paneFlickScrollListener.getGestureDetector().isPanning()) {
            float current = pane.getScrollX();
            float toClose = closedScrollValue - current;
            float toOpen = openedScrollValue - current;
            System.out.println(toClose+ " " + toOpen);
            current += 10 * Math.signum(Math.abs(toClose) < Math.abs(toOpen) ? toClose : toOpen);
            current = MathUtils.clamp(current, Math.min(closedScrollValue, openedScrollValue), Math.min(closedScrollValue, openedScrollValue));
            if (vertical) {
                pane.setScrollY(current);
            } else {
                pane.setScrollX(current);
            }
        }
        checkClosing(closedScrollValue);
    }

    private void checkClosing(float valueForClosing) {
        boolean previousState = closed;
        float scrollAmount = vertical ? pane.getScrollY() : pane.getScrollX();
        closed = (scrollAmount == valueForClosing);
        if (closed && !previousState) closedListener.run();
    }

    @Override
    public Container<ScrollPane> align(int align) {
        return super.align(align);
    }

    private void updateScrollBounds() {
        System.out.println("bounds update");
        switch (getAlign()) {
            case Align.right:
                openedScrollValue = 0;
                closedScrollValue = actorWrapper.getPrefWidth() * hideRatio;
            case Align.bottom:
                closedScrollValue = 0;
                openedScrollValue = actorWrapper.getPrefHeight() * hideRatio;
            case Align.top:
                openedScrollValue = 0;
                closedScrollValue = actorWrapper.getPrefHeight() * hideRatio;
            case Align.left:
                closedScrollValue = 0;
                openedScrollValue = actorWrapper.getPrefWidth() * hideRatio;
        }
    }
}
