package stonering.game.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import stonering.util.global.Logger;

import java.util.*;

/**
 * Consumes 1 keyTyped event right after keyDown. Both keyDown and keyTyped are passed further.
 *
 * @author Alexander on 06.09.2018.
 */
public class KeyBufferInputAdapter extends InputAdapter {

    private Set<Integer> keyBuffer;
    private HashMap<Character, Integer> keycodesMap;

    public KeyBufferInputAdapter() {
        keyBuffer = new HashSet<>();
        keycodesMap = new HashMap<>();
    }

    @Override
    public boolean keyDown(int keycode) {
        Logger.INPUT.logDebug("keyDown " + Input.Keys.toString(keycode));
        keyBuffer.add(keycode);           // next keyType with this will be skipped.
        return false;                     // continue
    }

    /**
     * Before keyType always goes keyDown, so first keyType after it should be skipped.
     */
    @Override
    public boolean keyTyped(char character) {
        int keycode = charToKeycode(character);
        if(keycode == -1) return true; // do not pass invalid characters
        if(keyBuffer.remove(keycode)) { // stop processing if key was in buffer
            return Logger.INPUT.logDebug(Input.Keys.toString(keycode) + " removed from buffer.", true);
        }
        Logger.INPUT.logDebug("keyTyped " + Input.Keys.toString(keycode));
        return false; // pass event further
    }

    /**
     * Translates typed character to corresponding keycode.
     * //TODO test letters, numbers, symbols.
     */
    private int charToKeycode(char character) {
        keycodesMap.putIfAbsent(character, Input.Keys.valueOf(Character.valueOf(character).toString().toUpperCase()));
        return keycodesMap.get(character);
    }
}
