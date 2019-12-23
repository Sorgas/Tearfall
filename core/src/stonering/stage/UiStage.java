package stonering.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.widget.util.Resizeable;

/**
 * Stage with screen viewport. Added widgets do no scaling on window resize.
 * Interception works for non-overridden methods (focused widget and mouse).
 *
 * @author Alexander on 20.02.2019.
 */
public class UiStage extends Stage implements Resizeable {
    public boolean interceptInput = true;

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
        return super.keyDown(keyCode) || interceptInput;
    }

    @Override
    public boolean keyUp(int keyCode) {
        return super.keyUp(keyCode) || interceptInput;
    }

    @Override
    public boolean keyTyped(char character) {
        return super.keyTyped(character) || interceptInput;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return super.touchDown(screenX, screenY, pointer, button) || interceptInput;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return super.touchDragged(screenX, screenY, pointer) || interceptInput;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return super.touchUp(screenX, screenY, pointer, button) || interceptInput;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return super.mouseMoved(screenX, screenY) || interceptInput;
    }

    @Override
    public boolean scrolled(int amount) {
        return super.scrolled(amount) || interceptInput;
    }
}
