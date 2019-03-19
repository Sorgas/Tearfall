package stonering.game.core.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.stages.base.UiStage;
import stonering.game.core.view.render.ui.menus.PauseMenu;
import stonering.util.global.Initable;

/**
 * Main in-game menu.
 *
 * @author Alexander on 09.11.2018.
 */
public class PauseMenuStage extends UiStage implements Initable {
    private PauseMenu pauseMenu;

    @Override
    public void init() {
        GameMvc.getInstance().getModel().setPaused(true);
        pauseMenu = new PauseMenu();
        Container container = new Container(pauseMenu).center();
        container.setFillParent(true);
        container.setDebug(true, true);
        this.addActor(container);
        pauseMenu.init();
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
