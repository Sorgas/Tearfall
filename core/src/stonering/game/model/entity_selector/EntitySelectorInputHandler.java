package stonering.game.model.entity_selector;

import com.badlogic.gdx.Gdx;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.BoxSelectionAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * System for moving, making selection and creating frame of {@link EntitySelector}. Is part of {@link EntitySelectorSystem}
 *
 * @author Alexander on 13.01.2020
 */
public class EntitySelectorInputHandler {
    private EntitySelectorSystem system;
    private EntitySelector selector;
    private BoxSelectionAspect box;
    private Position cachePosition = new Position();
    public boolean allowChangingZLevelOnSelection = true;
    public int shiftOffset = 10;

    public EntitySelectorInputHandler(EntitySelectorSystem system) {
        this.system = system;
        selector = system.selector;
        box = selector.get(BoxSelectionAspect.class);
    }

    /**
     * Starts box in selector's position. Commits it if box selection is disabled.
     */
    public void startSelection() {
        if (box.boxStart != null) {
            commitSelection(); // finish started selection
        } else {
            box.boxStart = selector.position.clone(); // start box
            if (!box.boxEnabled) commitSelection(); // end selection with single tile box
        }
    }

    /**
     * Calls handling of selected area. Does nothing if box start not set (was cancelled).
     */
    public void commitSelection() {
        if (box.boxStart == null) return;
        system.handleSelection();
        box.boxStart = null;
    }

    /**
     * Clears selection box start, preventing it from commit.
     * If box start is clear, calls cancellation in system for resetting tool.
     */
    public void cancelSelection() {
        if (box.boxStart == null) {
            system.handleCancel();
        } else {
            box.boxStart = null;
        }
    }

    public boolean moveByKey(int keycode) {
        int offset = Gdx.input.isKeyPressed(SHIFT_LEFT) ? shiftOffset : 1;
        boolean noSelection = selector.get(BoxSelectionAspect.class).boxStart == null;
        switch (keycode) {
            case W:
                system.moveSelector(0, offset, 0);
                return true;
            case A:
                system.moveSelector(-offset, 0, 0);
                return true;
            case S:
                system.moveSelector(0, -offset, 0);
                return true;
            case D:
                system.moveSelector(offset, 0, 0);
                return true;
            case R:
                if (noSelection || allowChangingZLevelOnSelection) system.moveSelector(0, 0, 1);
                return true;
            case F:
                if (noSelection || allowChangingZLevelOnSelection) system.moveSelector(0, 0, -1);
                return true;
        }
        return false;
    }

    /**
     * For moving diagonally when to keys are pressed.
     */
    public boolean secondaryMove(int keycode) { // same z-level only
        switch (keycode) {
            case W:
            case S:
                if (Gdx.input.isKeyPressed(A)) moveByKey(A);
                if (Gdx.input.isKeyPressed(D)) moveByKey(D);
                break;
            case A:
            case D:
                if (Gdx.input.isKeyPressed(W)) moveByKey(W);
                if (Gdx.input.isKeyPressed(S)) moveByKey(S);
        }
        return true;
    }
}
