package stonering.game.view.render.ui.lists;

/**
 * Interface for widgets which can be fetched with scrolling keys.
 *
 * @author Alexander Kuzyakov
 */
public interface Navigable {

    void up();

    void down();

    void select();
}
