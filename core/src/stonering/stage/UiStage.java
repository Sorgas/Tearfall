package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.widget.util.Resizeable;

/**
 * Stage with screen viewport. Added widgets do no scaling on window resize.
 * Can intercept input.
 *
 * @author Alexander on 20.02.2019.
 */
public class UiStage extends Stage implements Resizeable {
    protected boolean interceptInput = false;

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

    @Override
    public boolean keyDown(int keyCode) {
        return interceptInput || super.keyDown(keyCode);
    }

    @Override
    public boolean keyUp(int keyCode) {
        return interceptInput || super.keyUp(keyCode);
    }

    @Override
    public boolean keyTyped(char character) {
        return interceptInput || super.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return interceptInput || super.touchDown(screenX, screenY, pointer, button);
    }
}
