package stonering.stage.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.GameSettings;
import stonering.widget.util.Resizeable;

/**
 * Stage with screen viewport.
 * Widgets are scaled with {@link GameSettings#UI_SCALE} and do no scaling on window resize..
 * Interception works for non-overridden methods (focused widget and mouse).
 *
 * @author Alexander on 20.02.2019.
 */
public class UiStage extends Stage implements Resizeable {
    private ScreenViewport viewport;
    public boolean interceptInput = true;
    private float uiScale;

    public UiStage() {
        super();
        uiScale = GameSettings.UI_SCALE.VALUE;
        setViewport(viewport = new ScreenViewport());
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void setUiScale(float value) {
        viewport.setUnitsPerPixel(1 / value);
        getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
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
