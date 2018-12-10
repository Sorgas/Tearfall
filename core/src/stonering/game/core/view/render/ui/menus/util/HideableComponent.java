package stonering.game.core.view.render.ui.menus.util;

/**
 * Component which could be shown or hidden from some dynamic parent.
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
