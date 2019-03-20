package stonering.game.core.view.render.stages.base;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * Stage with screen viewport. Added widgets do no scaling on window resize.
 *
 * @author Alexander on 20.02.2019.
 */
public class UiStage extends Stage implements Resizeable{

    public UiStage() {
        super();
        setViewport(new ScreenViewport());
    }

    @Override
    public void draw() {
        getViewport().apply();
        act();
        super.draw();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
        getCamera().viewportWidth = width;
        getCamera().viewportHeight = height;
        getCamera().update();
    }
}
