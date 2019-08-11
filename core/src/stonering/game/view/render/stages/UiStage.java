package stonering.game.view.render.stages;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.game.view.render.util.Resizeable;

/**
 * Stage with screen viewport. Added widgets do no scaling on window resize.
 *
 * @author Alexander on 20.02.2019.
 */
public class UiStage extends Stage implements Resizeable {

    public UiStage() {
        super();
        setViewport(new ScreenViewport());
    }

    @Override
    public void draw() {
        act();
        super.draw();
    }

    public void resize(int width, int height) {
        getViewport().update(width, height, true);
    }
}
