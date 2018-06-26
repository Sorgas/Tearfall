package stonering.utils.global;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Alexander Kuzyakov on 30.06.2017.
 */
public class NavigationInputBuffer implements InputProcessor {
    private int maxDelay = 4;
    private int[] keys = new int[]{Input.Keys.UP, Input.Keys.DOWN, Input.Keys.LEFT, Input.Keys.RIGHT, Input.Keys.NUMPAD_1, Input.Keys.NUMPAD_2, Input.Keys.NUMPAD_3, Input.Keys.NUMPAD_4, Input.Keys.NUMPAD_5, Input.Keys.NUMPAD_6};
    private List<Integer> delays;
    private List<Integer> queue;
    private int activeCount = 0;


    private List<Integer> maxDelays;


    public NavigationInputBuffer(int maxDelay) {
        this.maxDelay = maxDelay;
        delays = new LinkedList<>();
        maxDelays = new LinkedList<>();
        queue = new LinkedList<>();
    }

    public int getInput() {
        if (activeCount > 0) {
            return queue.remove(0);
        }

        return 0;
    }

    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        for (int i = 0; i < keys.length; i++) {
            if (character == keys[i]) {
                queue.add((int) character);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
