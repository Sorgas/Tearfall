package stonering.desktop.sidebar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;

/**
 * Sidebar is a widget that can be dragged in and out of the screen.
 * It consists from widget with size x, wrapped by container of larger size 1.8x, wrapped by scroll pane of size x.
 * Thus, widget is always visible for 20-100% and scroll pane allows to move it from and into the screen.
 *
 * @author Alexander on 23.12.2019.
 */
public class Sidebar<T extends Actor> extends Container<ScrollPane> {
    private T actor;
    private float hideRatio; // part of widget that can be hidden.
    private boolean vertical;

    public Sidebar(T actor, int align, float hideRatio) {
        this.actor = actor;
        this.hideRatio = hideRatio;
        align = normalizeAlign(align);
        vertical = align == Align.top || align == Align.bottom;
        setActor(createPane(createWideContainer(new Container<>(actor).align(align))));
        size(actor.getWidth(), actor.getHeight()); // size the scroll pane
        createInterceptListener();
    }

    public Sidebar(T actor, int align) {
        this(actor, align, 0.8f);
    }

    private Container createWideContainer(Container<T> innerContainer) {
        Container container = new Container<>(innerContainer);
        container.size(
                actor.getWidth() * (1 + (!vertical ? hideRatio : 0)), // expand for scrolling horizontally
                actor.getHeight() * (1 + (vertical ? hideRatio : 0))); // expand for scrolling vertically
        return container;
    }

    private ScrollPane createPane(Container innerContainer) {
        ScrollPane pane = new ScrollPane(innerContainer);
        pane.setOverscroll(false, false);
        pane.setScrollingDisabled(vertical, !vertical); // allow to scroll by one axis
        pane.setFlickScroll(true);
        pane.getListeners().removeIndex(pane.getListeners().size - 1);
        return pane;
    }

    private void createInterceptListener() {
        getCaptureListeners().insert(0, new InputListener() { // blocks scroll start, if clicked outside actor
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (!actor.isAscendantOf(hit(x, y, false))) event.stop();
                return false;
            }
        });
    }

    /**
     * Handles non orthogonal {@link Align} values. Horizontal values is prioritized.
     */
    private int normalizeAlign(int align) {
        switch (align) {
            case Align.bottom:
            case Align.top:
            case Align.left:
            case Align.right:
                return align;
            case Align.topLeft:
            case Align.bottomLeft:
                return Align.left;
            case Align.topRight:
            case Align.bottomRight:
                return Align.right;
        }
        throw new IllegalArgumentException("Value " + align + " is not part of com.badlogic.gdx.utils.Align");
    }
}
