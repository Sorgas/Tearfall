package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * {@link Sidebar} that is closed if not extended more than 50% and opened fully otherwise.
 *
 * @author Alexander on 07.01.2020
 */
public class AutoClosedSidebar<T extends Actor> extends Sidebar<T> {
    private ActorGestureListener paneFlickScrollListener;
    private int minScroll;
    private int maxScroll;

    public AutoClosedSidebar(T widget, int align, float hideRatio) {
        super(widget, align, hideRatio);
        paneFlickScrollListener = (ActorGestureListener) pane.getListeners().get(0);
        minScroll = 0;
        maxScroll = (int) ((vertical ? actorWrapper.getPrefHeight() : actorWrapper.getPrefWidth()) * hideRatio);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!paneFlickScrollListener.getGestureDetector().isPanning()) {
            float currentScrollAmount = vertical ? pane.getScrollY() : pane.getScrollX();
            currentScrollAmount += currentScrollAmount < (maxScroll / 2) ? -10 : 10;
            currentScrollAmount = MathUtils.clamp(currentScrollAmount, minScroll, maxScroll);
            if (vertical) {
                pane.setScrollY(currentScrollAmount);
            } else {
                pane.setScrollX(currentScrollAmount);
            }
        }
    }
}
