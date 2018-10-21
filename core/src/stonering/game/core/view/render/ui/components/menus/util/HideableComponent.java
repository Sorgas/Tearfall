package stonering.game.core.view.render.ui.components.menus.util;

import stonering.game.core.view.render.ui.components.menus.Toolbar;

/**
 * Component which could be shown or hidden from {@link Toolbar}
 *
 * @author Alexander Kuzyakov
 */
public interface HideableComponent {

    /**
     * Adds all buttons from map to table and adds table to Toolbar widget.
     */
    void show();

    /**
     * Removes from its container.
     */
    void hide();
}
