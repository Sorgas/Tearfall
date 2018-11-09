package stonering.game.core.view.render.ui.menus.util;

/**
 * @author Alexander on 06.09.2018.
 */
public interface MouseInvocable extends Invokable {
    int CLICK = 0;
    int UP = 1;
    int DOWN = 2;

    boolean invoke(int modelX, int modelY, int button, int action);
}
