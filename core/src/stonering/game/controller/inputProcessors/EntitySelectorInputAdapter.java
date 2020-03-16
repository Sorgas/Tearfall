package stonering.game.controller.inputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.EntitySelectorInputHandler;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;
import stonering.util.ui.EnableableInputAdapter;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Handles button presses and mouse movement for entitySelector navigation and activation.
 * Calls {@link EntitySelectorInputHandler} for handling events.
 * Can be disabled.
 *
 * @author Alexander Kuzyakov
 */
public class EntitySelectorInputAdapter extends EnableableInputAdapter {

    public EntitySelectorInputAdapter() {
        super(new EntitySelectorInnerAdapter());
    }

    private static class EntitySelectorInnerAdapter extends InputAdapter {
        private EntitySelectorSystem system;
        private Position cachePosition;

        public EntitySelectorInnerAdapter() {
            system = GameMvc.model().get(EntitySelectorSystem.class);
            cachePosition = new Position();
        }

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.E:
                    if (system.selector.get(SelectionAspect.class).boxStart != null) { // finish started box at current position
                        Logger.INPUT.logDebug("committing selection box on E key");
                        system.inputHandler.commitSelection();
                    } else { // start box at current position
                        Logger.INPUT.logDebug("starting selection box on E key");
                        system.inputHandler.startSelection(null);
                    }
                    return true;
                case Input.Keys.Q:
                    Logger.INPUT.logDebug("cancelling selection box on Q key");
                    system.inputHandler.cancelSelection();
                    return true;
                case Input.Keys.T:
                    Logger.INPUT.logDebug("rotating entity selector");
                    system.rotateSelector(!Gdx.input.isKeyPressed(SHIFT_LEFT)); // counter clockwise with Shift + T
                default: // move selector if navigation key is pressed
                    return system.inputHandler.moveByKey(keycode);
            }
        }

        @Override
        public boolean keyTyped(char character) {
            int keycode = charToKeycode(character);
            return system.inputHandler.moveByKey(keycode) && system.inputHandler.secondaryMove(keycode); // supports diagonal move when two keys are pressed
        }

        @Override
        public boolean scrolled(int amount) {
            system.inputHandler.moveSelector(0, 0, amount);
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            system.inputHandler.setSelectorPosition(castScreenToModelCoords(screenX, screenY));
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            system.inputHandler.setSelectorPosition(castScreenToModelCoords(screenX, screenY));
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) { // lmb press starts selection
            if (button != Input.Buttons.LEFT) return false;
            Logger.INPUT.logDebug("starting selection box on LMB press");
            system.inputHandler.startSelection(cachePosition.set(castScreenToModelCoords(screenX, screenY)).clone());
            return true;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            switch (button) {
                case Input.Buttons.LEFT:
                    Logger.INPUT.logDebug("committing selection box on lMB release");
                    system.inputHandler.commitSelection();
                    return true;
                case Input.Buttons.RIGHT:
                    Logger.INPUT.logDebug("cancelling selection box on RMB release");
                    system.inputHandler.cancelSelection();
                    return true;
                default:
                    return false;
            }
        }

        /**
         * Used on clicks and mouse moves. Uses current z-level as output z.
         */
        private Position castScreenToModelCoords(int screenX, int screenY) {
            Vector3 batchCoords = GameMvc.view().localWorldStage.getCamera().unproject(new Vector3(screenX, screenY, 0));
            AtlasesEnum atlas = AtlasesEnum.blocks; // use blocks sizes
            int heightToSkip = system.selector.position.z * atlas.HEIGHT + (atlas.hasToppings ? atlas.TOPPING_HEIGHT : 0);
            return new Position((int) batchCoords.x / atlas.WIDTH, // int casts is mandatory
                    (int) (batchCoords.y - heightToSkip) / atlas.DEPTH,
                    system.selector.position.z);
        }

        private int charToKeycode(char character) {
            return valueOf(Character.valueOf(character).toString().toUpperCase());
        }
    }
}
