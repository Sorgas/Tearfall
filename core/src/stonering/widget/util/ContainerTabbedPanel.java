package stonering.widget.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.game.GameMvc;

/**
 * @author Alexander on 23.12.2019.
 */
public class ContainerTabbedPanel extends TabbedPanel<Container> {

    public ContainerTabbedPanel() {
        super();
        addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode != Input.Keys.Q) return false;
                close();
                return true;
            }
        });
    }

    public void close() {
        GameMvc.instance().view().removeStage(getStage());
    }
}
