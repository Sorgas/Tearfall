package stonering.game.controller.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.global.Executor;
import stonering.util.global.Logger;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Handles button presses and mouse movement for entitySelector navigation. Can be disabled.
 * Has handler for handling clicks and selection.
 *
 * @author Alexander Kuzyakov
 */
public class EntitySelectorInputAdapter extends InputAdapter {
    private EntitySelector selector;
    private boolean enabled;
    private final Executor defaultSelectHandler;
    public Executor selectHandler;
    private Position cachePosition;
    
    public EntitySelectorInputAdapter() {
        selector = GameMvc.instance().model().get(EntitySelector.class);
        enabled = true;
        defaultSelectHandler = this::showEntityStage;
        cachePosition = new Position();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!enabled) return false;
        if(keycode == Input.Keys.E) {
            Logger.INPUT.logDebug("handling E in EntitySelectorInputAdapter");
            handleSelection();
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
        selector.position.set(getModelPosition(screenX, screenY));
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        selector.position.set(getModelPosition(screenX, screenY));
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
        handleSelection();
        System.out.println(selector.position);
        return true;
    }

    /**
     * Finds model position of screen point in current z-level.
     */
    private Position getModelPosition(int screenX, int screenY) {
        Vector3 batchCoords = GameMvc.instance().view().localWorldStage.getCamera().unproject(new Vector3(screenX, screenY, 0));
        AtlasesEnum atlas = AtlasesEnum.blocks;
        int heightToSkip = selector.position.z * atlas.HEIGHT + (atlas.hasToppings ? atlas.TOPPING_HEIGHT : 0);
        int x = (int) batchCoords.x / atlas.WIDTH;
        int y = ((int) batchCoords.y - heightToSkip) / atlas.DEPTH;
        return cachePosition.set(x,y, selector.position.z);
    }

    private void handleSelection() {
        (selectHandler != null ? selectHandler : defaultSelectHandler).execute(); // use dynamic handler if possible
    }

    private int charToKeycode(char character) {
        return valueOf(Character.valueOf(character).toString().toUpperCase());
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void showEntityStage() {
        GameMvc.instance().view().showEntityStage(selector.position);
    }
}
