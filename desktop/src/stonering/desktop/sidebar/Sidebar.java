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
    private float hideRatio = 0.8f; // part of widget that can be hidden.

    public Sidebar(T actor, float hideRatio) {
        this.actor = actor;
        this.hideRatio = hideRatio;
    }

    public Sidebar(T actor) {
        this.actor = actor;
        createContainer();
    }

    private void createContainer() {
        Container wrapContainer = new Container<>(actor);
        wrapContainer.size(actor.getWidth(), actor.getHeight());
        wrapContainer.align(Align.right);

        Container wideContainer = new Container<>(wrapContainer);
        wideContainer.width(actor.getWidth() * (1 + hideRatio));

        ScrollPane pane = new ScrollPane(wideContainer);
        pane.setOverscroll(false, false);
        pane.setFlickScroll(true);
        pane.setSize(actor.getWidth(), actor.getHeight());

        setActor(pane);
        width(actor.getWidth());
    }
}
