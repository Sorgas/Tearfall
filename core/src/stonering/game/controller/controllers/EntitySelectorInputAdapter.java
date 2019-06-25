package stonering.game.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;

/**
 * Handles button presses for entitySelector navigation. Can be disabled.
 *
 * @author Alexander Kuzyakov
 */
public class EntitySelectorInputAdapter extends InputAdapter {
    private EntitySelector selector;
    private boolean enabled;

    public EntitySelectorInputAdapter() {
        selector = GameMvc.instance().getModel().get(EntitySelector.class);
        enabled = true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (enabled) {
            int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
            switch (keycode) {
                case Input.Keys.W:
                    selector.moveSelector(0, offset, 0);
                    return true;
                case Input.Keys.A:
                    selector.moveSelector(-offset, 0, 0);
                    return true;
                case Input.Keys.S:
                    selector.moveSelector(0, -offset, 0);
                    return true;
                case Input.Keys.D:
                    selector.moveSelector(offset, 0, 0);
                    return true;
                case Input.Keys.R:
                    selector.moveSelector(0, 0, 1);
                    return true;
                case Input.Keys.F:
                    selector.moveSelector(0, 0, -1);
                    return true;
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (enabled) {
            switch (charToKeycode(character)) {
                case Input.Keys.W:
                case Input.Keys.A:
                case Input.Keys.S:
                case Input.Keys.D:
                    int offset = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
                    if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                        selector.moveSelector(0, offset, 0);
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                        selector.moveSelector(0, -offset, 0);
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                        selector.moveSelector(-offset, 0, 0);
                    }
                    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                        selector.moveSelector(offset, 0, 0);
                    }
                    return true;
                case Input.Keys.R:
                    selector.moveSelector(0, 0, 1);
                    return true;
                case Input.Keys.F:
                    selector.moveSelector(0, 0, -1);
                    return true;
            }
        }
        return false;
    }

    private int charToKeycode(char character) {
        return Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase());
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
