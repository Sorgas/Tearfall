package stonering.game.controller.inputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.EntitySelectorInputHandler;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.util.controller.EnableableInputAdapter;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * Handles keyboard and mouse input for {@link EntitySelector} navigation and activation.
 * LMB press - start selection.
 * LMB release - confirm selection.
 * RMB press - cancel selection.
 * E press - start or confirm selection.
 * Q press - cancel selection.
 * WASDRF(Shift) - navigation.
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

        public EntitySelectorInnerAdapter() {
            system = GameMvc.model().get(EntitySelectorSystem.class);
        }

        @Override
        public boolean keyDown(int keycode) {
            switch (keycode) {
                case Input.Keys.E:
                    system.inputHandler.startSelection();
                    return true;
                case Input.Keys.Q:
                    return system.cancelSelection();
                case Input.Keys.T:
                    system.rotateSelector(!Gdx.input.isKeyPressed(SHIFT_LEFT)); // co unter clockwise with Shift + T

                default: // move selector if navigation key is pressed
                    return system.inputHandler.moveByKey(keycode);
            }
        }

        @Override
        public boolean keyTyped(char character) {
            int keycode = charToKeycode(character);
            if(keycode == Input.Keys.T) system.rotateSelector(!Gdx.input.isKeyPressed(SHIFT_LEFT)); // co unter clockwise with Shift + T
            return system.inputHandler.moveByKey(keycode) && system.inputHandler.secondaryMove(keycode); // supports diagonal move when two keys are pressed
        }

        @Override
        public boolean scrolled(int amount) {
            system.moveSelector(0, 0, amount);
            return true;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            system.setSelectorPosition(castScreenToModelCoords(screenX, screenY));
            return true;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            system.setSelectorPosition(castScreenToModelCoords(screenX, screenY));
            return true;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) { // lmb press starts selection
            switch(button) {
                case Input.Buttons.LEFT:
                    system.inputHandler.startSelection();
                    return true;
                case Input.Buttons.RIGHT:
//                    if(!system.inputHandler.cancelSelection())

                    return true;
            }
            return false;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            switch (button) {
                case Input.Buttons.LEFT:
                    system.inputHandler.commitSelection();
                    return true;
                case Input.Buttons.RIGHT:
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
            AtlasesEnum atlas = AtlasesEnum.blocks; // use blocks sizes as grid size
            int heightToSkip = system.selector.position.z * atlas.HEIGHT + atlas.TOPPING_HEIGHT;
            return new Position((int) batchCoords.x / atlas.WIDTH, // int casts is mandatory
                    (int) (batchCoords.y - heightToSkip) / atlas.DEPTH,
                    system.selector.position.z);
        }

        private int charToKeycode(char character) {
            return valueOf(Character.valueOf(character).toString().toUpperCase());
        }
    }
}
