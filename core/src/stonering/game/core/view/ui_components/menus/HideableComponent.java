package stonering.game.core.view.ui_components.menus;

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
