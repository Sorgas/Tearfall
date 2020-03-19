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

    public void setSelectorPosition(Position position) {
        GameMvc.model().get(LocalMap.class).normalizePosition(selector.position.set(position.x, position.y, position.z));
    }

    public boolean moveByKey(int keycode) {
        int offset = Gdx.input.isKeyPressed(SHIFT_LEFT) ? shiftOffset : 1;
        boolean noSelection = selector.get(BoxSelectionAspect.class).boxStart == null;
        switch (keycode) {
            case W:
                moveSelector(0, offset, 0);
                return true;
            case A:
                moveSelector(-offset, 0, 0);
                return true;
            case S:
                moveSelector(0, -offset, 0);
                return true;
            case D:
                moveSelector(offset, 0, 0);
                return true;
            case R:
                if (noSelection || allowChangingZLevelOnSelection) moveSelector(0, 0, 1);
                return true;
            case F:
                if (noSelection || allowChangingZLevelOnSelection) moveSelector(0, 0, -1);
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

    /**
     * General logic for moving selector. Selector can move to any position within map(considering selector size), see {@link LocalMap#normalizeRectangle}.
     * When box started, selector can move by number of tiles, multiple to selector size.
     */
    public void moveSelector(int dx, int dy, int dz) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        cachePosition.set(selector.position);
        if (box.boxStart != null) {
            if (selector.size.x > 0) dx = defineMoveDelta(dx, selector.size.x); // update delta for non 1-tile selector and non 1-tile move
            if (selector.size.y > 0) dy = defineMoveDelta(dy, selector.size.y);
        }
        selector.position.add(dx, dy, dz);
        map.normalizeRectangle(selector.position, selector.size.x, selector.size.y); // selector should not move out of map
        if (!cachePosition.equals(selector.position)) system.selectorMoved(); // updates if selector did move
    }

    public int defineMoveDelta(int delta, int size) {
        if (delta == 0) return 0;
        size += 1;
        return size * (delta > 0
                ? (delta - 1) / size + 1
                : (delta + 1) / size - 1);
    }
}
