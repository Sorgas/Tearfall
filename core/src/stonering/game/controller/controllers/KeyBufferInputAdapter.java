package stonering.game.controller.controllers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

import java.util.*;

/**
 * Skips 1 keyTyped event right after keyDown. Both keyDown and keyTyped are passed further.
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
        keyBuffer.add(keycode);           // next keyType with this will be skipped.
        return false;                     // continue
    }

    /**
     * Before keyType always goes keyDown, so first keyType after it should be skipped.
     */
    @Override
    public boolean keyTyped(char character) {
        return keyBuffer.remove(charToKeycode(character)); // stop processing if key was in buffer
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
