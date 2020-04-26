package stonering.desktop.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import stonering.enums.ControlActionsEnum;
import stonering.util.global.StaticSkin;

/**
 * {@link Tree}, that can handle navigation input.
 * TODO
 * @author Alexander on 25.04.2020
 */
public class NavigableTree extends Tree {
    public final Map<Integer, ControlActionsEnum> keyMapping; // additional keys to actions mapping.
    public Consumer<Node> selectListener = actor -> {}; // called on selection confirmation
    public Consumer<Node> navigationListener = actor -> {}; // called when selection changes
    public Runnable cancelListener = () -> {
    }; // called on Q
    public int selectedIndex = -1;

    public NavigableTree() {
        super(StaticSkin.getSkin());
        keyMapping = new HashMap<>();

        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch(keyMapping.get(keycode)) {
                    case UP:
                        break;
                    case DOWN:
                        break;
                    case LEFT:
                        break;
                    case RIGHT:
                        break;
                    case SELECT:
                        break;
                    case CANCEL:
                        break;
                }
                return true;
            }
        });
    }
}
