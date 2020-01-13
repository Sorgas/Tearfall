package stonering.game.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.system.EntitySelectorInputHandler;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Handles button presses and mouse movement for entitySelector navigation and activation.
 * Calls {@link EntitySelectorInputHandler} for handling events.
 * Can be disabled.
 *
 * @author Alexander Kuzyakov
 */
public class EntitySelectorInputAdapter extends InputAdapter {
    private EntitySelectorSystem system;
    public boolean enabled;
    private Position cachePosition;

    public EntitySelectorInputAdapter() {
        system = GameMvc.instance().model().get(EntitySelectorSystem.class);
        enabled = true;
        cachePosition = new Position();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (!enabled) return false;
        if(keycode == Input.Keys.E) {
            if(system.selector.getAspect(SelectorBoxAspect.class).boxStart != null) {
                Logger.INPUT.logDebug("committing selection box on E key");
                system.inputHandler.commitSelection();
            } else {
                Logger.INPUT.logDebug("starting selection box on E key");
                system.inputHandler.startSelection(null); // start box at current position
            }
            return true;
        }
        if(keycode == Input.Keys.Q) {
            Logger.INPUT.logDebug("cancelling selection box on Q key");
            system.inputHandler.cancelSelection();
            return true;
        }
        return system.inputHandler.moveByKey(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if (!enabled) return false;
        int keycode = charToKeycode(character);
        return system.inputHandler.moveByKey(keycode) && system.inputHandler.secondaryMove(keycode); // supports diagonal move when two keys are pressed
    }

    @Override
    public boolean scrolled(int amount) {
        if (!enabled) return false;
        system.inputHandler.moveSelector(0, 0, amount);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!enabled) return false;
        system.inputHandler.setSelectorPosition(cachePosition.set(castScreenToModelCoords(screenX, screenY)));
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (enabled && button == Input.Buttons.LEFT) {
            Logger.INPUT.logDebug("starting selection box on LMB press");
            system.inputHandler.startSelection(cachePosition.set(castScreenToModelCoords(screenX, screenY)));
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (enabled) {
            if (button == Input.Buttons.LEFT) {
                Logger.INPUT.logDebug("committing selection box on lMB release");
                system.inputHandler.commitSelection();
                return true;
            }
            if (button == Input.Buttons.RIGHT) {
                Logger.INPUT.logDebug("cancelling selection box on RMB release");
                system.inputHandler.cancelSelection();
                return true;
            }
        }
        return false;
    }

    private Vector3 castScreenToModelCoords(int screenX, int screenY) {
        Vector3 batchCoords = GameMvc.instance().view().localWorldStage.getCamera().unproject(new Vector3(screenX, screenY, 0));
        AtlasesEnum atlas = AtlasesEnum.blocks; // use blocks sizes
        int heightToSkip = system.selector.position.z * atlas.HEIGHT + (atlas.hasToppings ? atlas.TOPPING_HEIGHT : 0);
        batchCoords.x /= atlas.WIDTH;
        batchCoords.y = (batchCoords.y - heightToSkip) / atlas.DEPTH;
        return batchCoords;
    }

    private int charToKeycode(char character) {
        return valueOf(Character.valueOf(character).toString().toUpperCase());
    }
}
