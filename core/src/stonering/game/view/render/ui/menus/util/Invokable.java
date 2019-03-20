package stonering.game.view.render.ui.menus.util;

/**
 * Interface for component that could be invoked with codes of pressed keys to handle input.
 *
 * @author Alexander Kuzyakov
 */
public interface Invokable {
    boolean invoke(int keycode);
}
