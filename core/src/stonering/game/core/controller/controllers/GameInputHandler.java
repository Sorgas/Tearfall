package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.GameMvc;
import stonering.game.core.view.GameView;
import stonering.game.core.view.render.ui.menus.util.Invokable;

import java.util.*;

/**
 * Handles non-pause in the game.
 * Transforms all key events(down, typed) to keyDown for easier handling on stage ui actors.
 * First priority are menus in iuDrawer, then camera.
 * Handles case of skipping keyTyped right after keyDown.
 *
 * @author Alexander on 06.09.2018.
 */
public class GameInputHandler extends InputAdapter {
    private Stage stage;   // updated from gameView
    private CameraInputHandler cameraInputHandler;

    private Set<Integer> charsToSkip;
    private HashMap<Character, Integer> keycodesMap;

    public GameInputHandler(GameMvc gameMvc) {
        charsToSkip = new HashSet<>();
        cameraInputHandler = new CameraInputHandler(gameMvc);
        keycodesMap = new HashMap<>();
    }
// TODO mouse input
//    public boolean handleEvent(int screenX, int screenY, int button, int action) {
//        Vector2 vector2 = gameView.getWorldDrawer().translateScreenPositionToModel(new Vector2(screenX, screenY));
//        return gameView.invoke(Math.round(vector2.x), Math.round(vector2.y), button, action);
//    }

    @Override
    public boolean keyDown(int keycode) {
        charsToSkip.add(keycode);           // next keyType with this will be skipped.
        return !stage.keyDown(keycode) && cameraInputHandler.keyDown(keycode); // call stage, then camera
    }

    /**
     * Before keyType always goes keyDown, so first keyType after it should be skipped.
     */
    @Override
    public boolean keyTyped(char character) {
        int keycode = charToKeycode(character);
        if (charsToSkip.contains(keycode)) {
            charsToSkip.remove(keycode); // skip character, do not handle
            return false;
        } else {
            return !stage.keyDown(keycode) && cameraInputHandler.typeCameraKey(keycode); // call stage, then camera
        }
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
