package stonering.game.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.util.global.Logger;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Handles button presses and mouse movement for entitySelector navigation and activation.
 * Calls {@link EntitySelectorSystem} for handling events.
 * Can be disabled.
 *
 * @author Alexander Kuzyakov
 */
public class EntitySelectorInputAdapter extends InputAdapter {
    private EntitySelectorSystem system;
    public boolean enabled;
    
    public EntitySelectorInputAdapter() {
        system = GameMvc.instance().model().get(EntitySelectorSystem.class);
        enabled = true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!enabled) return false;
        if(keycode == Input.Keys.E) {
            Logger.INPUT.logDebug("handling E in EntitySelectorInputAdapter");
            system.handleEKeyDown();
            return true;
        }
        if(keycode == Input.Keys.Q) {
            Logger.INPUT.logDebug("handling Q in EntitySelectorInputAdapter");
            system.cancelSelectionBox();
            return true;
        }
        return moveByKey(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (!enabled) return false;
        int keycode = charToKeycode(character);
        return moveByKey(keycode) && secondaryMove(keycode); // supports diagonal move when two keys are pressed
    }

    private boolean moveByKey(int keycode) {
        int offset = Gdx.input.isKeyPressed(SHIFT_LEFT) ? 10 : 1;
        switch (keycode) {
            case W:
                system.moveSelector(0, offset, 0);
                return true;
            case A:
                system.moveSelector(-offset, 0, 0);
                return true;
            case S:
                system.moveSelector(0, -offset, 0);
                return true;
            case D:
                system.moveSelector(offset, 0, 0);
                return true;
            case R:
                system.moveSelector(0, 0, 1);
                return true;
            case F:
                system.moveSelector(0, 0, -1);
        }
        return false;
    }

    private boolean secondaryMove(int keycode) {
        switch (keycode) {
            case W:
            case S:
                if(Gdx.input.isKeyPressed(A)) moveByKey(A);
                if(Gdx.input.isKeyPressed(D)) moveByKey(D);
                break;
            case A:
            case D:
                if(Gdx.input.isKeyPressed(W)) moveByKey(W);
                if(Gdx.input.isKeyPressed(S)) moveByKey(S);
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        if (!enabled) return false;
        system.moveSelector(0, 0, amount);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!enabled) return false;
        system.updateSelectorPositionByScreenCoords(screenX, screenY);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (enabled && button == Input.Buttons.LEFT) {
            system.handleLeftTouchDown(screenX, screenY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (enabled) {
            if (button == Input.Buttons.LEFT) {
                system.handleSelection(); // finish box or tile selection
                return true;
            }
            if (button == Input.Buttons.RIGHT) {
                system.handleCancel();
                return true;
            }
        }
        return false;
    }

    private int charToKeycode(char character) {
        return valueOf(Character.valueOf(character).toString().toUpperCase());
    }
}
