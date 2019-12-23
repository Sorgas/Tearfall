package stonering.desktop.sidebar;

import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private int align;
    private boolean vertical;

    public Sidebar(T actor, int align, float hideRatio) {
        this.actor = actor;
        this.hideRatio = hideRatio;
        this.align = normalizeAlign(align);
        vertical = this.align == Align.top || this.align == Align.bottom;
        setActor(createPane(createWideContainer(wrapActor(actor))));
        size(actor.getWidth(), actor.getHeight());
    }

    public Sidebar(T actor, int align) {
        this(actor, align, 0.8f);
    }

    private Container wrapActor(T actor) {
        Container container = new Container<>(actor);
        container.size(actor.getWidth(), actor.getHeight());
        container.align(align);
        return container;
    }

    private Container createWideContainer(Container<T> innerContainer) {
        Container wideContainer = new Container<>(innerContainer);
        if(vertical) {
            wideContainer.height(actor.getHeight() * (1 + hideRatio));
        } else {
            wideContainer.width(actor.getWidth() * (1 + hideRatio));
        }
        return wideContainer;
    }

    private ScrollPane createPane(Container innerContainer) {
        ScrollPane pane = new ScrollPane(innerContainer);
        pane.setOverscroll(false, false);
        pane.setScrollingDisabled(vertical, !vertical);
        pane.setFlickScroll(true);
        pane.setSize(actor.getWidth(), actor.getHeight());
        return pane;
    }

    /**
     * Handles non orthogonal {@link Align} values. Horizontal values is prioritized.
     */
    private int normalizeAlign(int align) {
        switch (align) {
            case Align.bottom :
            case Align.top :
            case Align.left :
            case Align.right :
                return align;
            case Align.topLeft :
            case Align.bottomLeft :
                return Align.left;
            case Align.topRight :
            case Align.bottomRight :
                return Align.right;
        }
        throw new IllegalArgumentException("Value " + align + " is not part of com.badlogic.gdx.utils.Align");
    }
}
