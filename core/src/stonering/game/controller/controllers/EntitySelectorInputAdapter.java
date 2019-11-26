package stonering.game.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Handles button presses for entitySelector navigation. Can be disabled.
 *
 * @author Alexander Kuzyakov
 */
public class EntitySelectorInputAdapter extends InputAdapter {
    private EntitySelector selector;
    private boolean enabled;

    public EntitySelectorInputAdapter() {
        selector = GameMvc.instance().model().get(EntitySelector.class);
        enabled = true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!enabled) return false;
        if(keycode == Input.Keys.E) {
            GameMvc.instance().getView().showEntityStage(selector.position);
            return true;
        }
        int offset = Gdx.input.isKeyPressed(SHIFT_LEFT) ? 10 : 1;
        return moveByKey(keycode, offset);
    }

    @Override
    public boolean keyTyped(char character) {
        if (!enabled) return false;
        int keycode = charToKeycode(character);
        int offset = Gdx.input.isKeyPressed(SHIFT_LEFT) ? 10 : 1;
        return moveByKey(keycode, offset) && secondaryMove(keycode, offset);
    }

    private boolean moveByKey(int keycode, int offset) {
        switch (keycode) {
            case W:
                selector.moveSelector(0, offset, 0);
                return true;
            case A:
                selector.moveSelector(-offset, 0, 0);
                return true;
            case S:
                selector.moveSelector(0, -offset, 0);
                return true;
            case D:
                selector.moveSelector(offset, 0, 0);
                return true;
            case R:
                selector.moveSelector(0, 0, 1);
                return true;
            case F:
                selector.moveSelector(0, 0, -1);
        }
        return false;
    }

    private boolean secondaryMove(int keycode, int offset) {
        switch (keycode) {
            case W:
            case S:
                if(Gdx.input.isKeyPressed(A)) moveByKey(A, offset);
                if(Gdx.input.isKeyPressed(D)) moveByKey(D, offset);
                break;
            case A:
            case D:
                if(Gdx.input.isKeyPressed(W)) moveByKey(W, offset);
                if(Gdx.input.isKeyPressed(S)) moveByKey(S, offset);
        }
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        selector.moveSelector(0, 0, amount);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        System.out.println(GameMvc.instance().getView().localWorldStage.getCamera().unproject(new Vector3(screenX, screenY, 0)));
        int selectorZ = selector.position.z;
//        selector.setPosition();
        return false;
    }

    private int charToKeycode(char character) {
        return valueOf(Character.valueOf(character).toString().toUpperCase());
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
