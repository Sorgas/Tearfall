package stonering.game.core.view.render.ui.components.menus.util;

/**
 * Interface that adds chaining logic to actors (like wizard menus).
 *
 *
 * @author Alexander on 23.10.2018.
 */
public interface SequencedWidget {

    SequencedWidget getNext();

    boolean hasNext();
}
