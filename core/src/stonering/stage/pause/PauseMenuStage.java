package stonering.stage.pause;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.game.GameMvc;
import stonering.stage.util.UiStage;
import stonering.util.lang.Initable;

/**
 * Main in-game menu.
 *
 * @author Alexander on 09.11.2018.
 */
public class PauseMenuStage extends UiStage implements Initable {
    private PauseMenu pauseMenu;

    @Override
    public void init() {
        GameMvc.model().gameTime.setPaused(true);
        pauseMenu = new PauseMenu();
        Container<PauseMenu> container = new Container<>(pauseMenu).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
        setKeyboardFocus(pauseMenu);
    }

    @Override
    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        getCamera().viewportWidth = width;
        getCamera().viewportHeight = height;
        getCamera().update();
    }
}
