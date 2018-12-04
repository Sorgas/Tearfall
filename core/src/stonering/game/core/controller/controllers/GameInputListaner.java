package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.GameMvc;

import java.util.*;

/**
 * Handles non-pause input in the game.
 * Transforms all key events(down, typed) to keyDown for easier handling on stage ui actors.
 * First priority are menus in iuDrawer, then camera.
 * Handles case of skipping keyTyped right after keyDown.
 *
 * @author Alexander on 06.09.2018.
 */
public class GameInputListaner extends InputListener {
    private Stage stage;   // updated from gameView
    private CameraInputHandler cameraInputHandler;

    private Set<Integer> charsToSkip;
    private HashMap<Character, Integer> keycodesMap;

    public GameInputListaner(GameMvc gameMvc) {
        charsToSkip = new HashSet<>();
        cameraInputHandler = new CameraInputHandler(gameMvc);
        keycodesMap = new HashMap<>();
    }


    @Override
    public boolean keyDown(InputEvent event, int keycode) {
        event.stop();
        charsToSkip.add(keycode);           // next keyType with this will be skipped.
        System.out.println("keyDown " + stage.getKeyboardFocus());
        return !stage.keyDown(keycode) && cameraInputHandler.keyDown(keycode); // call stage, than camera
    }

    /**
     * Before keyType always goes keyDown, so first keyType after it should be skipped.
     */
    @Override
    public boolean keyTyped(InputEvent event, char character) {
        int keycode = charToKeycode(character);
        if (keycode >= 0)
            if (charsToSkip.contains(keycode)) {
                charsToSkip.remove(keycode); // skip character, do not handle
                return false;
            } else {
                return !stage.keyDown(keycode) && cameraInputHandler.typeCameraKey(keycode); // call stage, then camera
            }
        return false;
    }

    /**
     * Translates typed character to corresponding keycode.
     * //TODO test letters, numbers, symbols.
     *
     * @param character
     * @return
     */
    private int charToKeycode(char character) {
        if (!keycodesMap.containsKey(character)) {
            keycodesMap.put(Character.valueOf(character), Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase()));
        }
        return keycodesMap.get(character);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}