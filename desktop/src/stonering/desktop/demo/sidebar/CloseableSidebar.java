package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

/**
 * Sidebar, that checks its state. It can be closed or open
 * @author Alexander on 24.01.2020.
 */
public class CloseableSidebar<T extends Actor> extends Sidebar<T> {
    protected float closedScrollValue;
    protected float openedScrollValue;
    protected Runnable closedListener; // called when sidebar is closed
    protected Runnable openedListener; // called when sidebar is opened
    protected boolean closed;
    protected boolean opened;

    public CloseableSidebar(T actor, int align, float hideRatio) {
        super(actor, align, hideRatio);
        closedListener = () -> System.out.println("closed");
        openedListener = () -> System.out.println("opened");
        updateScrollBounds();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        updateState();
    }

    private void updateState() {
        float current = getScrollAmount();
        if (!closed && current == closedScrollValue) closedListener.run();
        if (!opened && current == openedScrollValue) openedListener.run();
        closed = current == closedScrollValue;
        opened = current == openedScrollValue;
    }

    private void updateScrollBounds() {
        switch (getAlign()) {
            case Align.right:
                closedScrollValue = 0;
                openedScrollValue = actorWrapper.getPrefWidth() * hideRatio;
                break;
            case Align.bottom:
                closedScrollValue = 0;
                openedScrollValue = actorWrapper.getPrefHeight() * hideRatio;
                break;
            case Align.top:
                openedScrollValue = 0;
                closedScrollValue = actorWrapper.getPrefHeight() * hideRatio;
                break;
            case Align.left:
                openedScrollValue = 0;
                closedScrollValue = actorWrapper.getPrefWidth() * hideRatio;
        }
    }
}
