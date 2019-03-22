package stonering.game.view.render.ui.menus.util;

import java.util.function.Consumer;

/**
 * Shows that widget with this interface can be highlighted (to show its focused).
 * Should be performed in act method in widgets.
 *
 * @author Alexander
 */
public interface Highlightable {
    void setHighlightHandler(Consumer<Boolean> handler);
}
