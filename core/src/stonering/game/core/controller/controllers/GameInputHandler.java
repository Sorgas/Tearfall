package stonering.game.core.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import stonering.game.core.GameMvc;
import stonering.game.core.view.GameView;
import stonering.game.core.view.render.ui.menus.util.Invokable;

import java.util.*;

/**
 * Handles all input in the game.
 * First priority are menus in iuDrawer, then camera.
 *
 * @author Alexander on 06.09.2018.
 */
public class GameInputHandler extends Controller implements Invokable {
    private GameView gameView;
    private CameraInputHandler cameraInputHandler;
    private Set<Integer> charsToSkip;
    private HashMap<Character, Integer> keycodesMap;
    private ArrayList<Character> cameraInput = new ArrayList<>(Arrays.asList('a', 's', 'd', 'w', 'A', 'S', 'D', 'W', 'r', 'f', 'R', 'F'));

    public GameInputHandler(GameMvc gameMvc) {
        super(gameMvc);

        charsToSkip = new HashSet<>();
        cameraInputHandler = new CameraInputHandler(gameMvc);
        keycodesMap = new HashMap<>();
    }

    @Override
    public void init() {
        gameView = gameMvc.getView();
    }

    public boolean handleEvent(int screenX, int screenY, int button, int action) {
        Vector2 vector2 = gameView.getWorldDrawer().translateScreenPositionToModel(new Vector2(screenX, screenY));
        return gameView.invoke(Math.round(vector2.x), Math.round(vector2.y), button, action);
    }

    /**
     * Invoked on keyDown.
     *
     * @param keycode
     * @return
     */
    @Override
    public boolean invoke(int keycode) {
        charsToSkip.add(keycode);
        if (!gameView.invoke(keycode)) { // no ui is active
            cameraInputHandler.tryMoveCamera(keycode);
        }
        return true;
    }

    /**
     * Invoked on keyType. Before keyType always goes keyDown, so first keyType after it should be skipped.
     *
     * @param character
     * @return
     */
    public boolean typed(char character) {
        int keycode = charToKeycode(character);
        if(charsToSkip.contains(keycode)) {
            charsToSkip.remove(keycode); // skip character, do not handle
        } else {
            if (!gameView.invoke(keycode) && cameraInput.contains(character)) { // no ui is active
                cameraInputHandler.typeCameraKey(keycode);
            }
        }
        return true;
    }

    /**
     * Translates typed character to corresponding keycode.
     * //TODO test letters, numbers, symbols.
     * @param character
     * @return
     */
    private int charToKeycode(char character) {
        if(!keycodesMap.containsKey(character)) {
            keycodesMap.put(Character.valueOf(character), Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase()));
        }
        return keycodesMap.get(character);
    }
}
