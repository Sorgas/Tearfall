package stonering.util.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;

/**
 * Wrapper for input adapter that adds switching functionality to it.
 * If enabled, passes events to inner adapter, if disabled, returns false to all events.
 *
 * @author Alexander on 20.01.2020
 */
public abstract class EnableableInputAdapter implements InputProcessor {
    public final InputAdapter inner;
    public boolean enabled = true;

    public EnableableInputAdapter(InputAdapter inner) {
        this.inner = inner;
    }

    @Override
    public boolean keyDown(int keycode) {
        return enabled && inner.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        return enabled && inner.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        return enabled && inner.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return enabled && inner.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return enabled && inner.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return enabled && inner.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return enabled && inner.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(int amount) {
        return enabled && inner.scrolled(amount);
    }
}
