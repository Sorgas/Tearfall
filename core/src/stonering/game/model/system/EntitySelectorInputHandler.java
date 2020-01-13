package stonering.game.model.system;

import com.badlogic.gdx.Gdx;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectorBoxAspect;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

import javax.annotation.Nullable;

import static com.badlogic.gdx.Input.Keys.*;

/**
 * System for moving, making selection and creating frame of {@link EntitySelector}. Is part of {@link EntitySelectorSystem}
 *
 * @author Alexander on 13.01.2020
 */
public class EntitySelectorInputHandler {
    private EntitySelectorSystem system;
    private EntitySelector selector;
    private Position cachePosition;

    public EntitySelectorInputHandler(EntitySelectorSystem system) {
        this.system = system;
        selector = system.selector;
        cachePosition = new Position();
    }

    /**
     * Starts selection box if it's enabled, instantly commits box with single tile otherwise.
     * If position is null, uses selector's current position.
     */
    public void startSelection(@Nullable Position position) {
        if (position == null) position = selector.position;
        SelectorBoxAspect aspect = selector.getAspect(SelectorBoxAspect.class);
        if (aspect.enabled) {
            aspect.boxStart = position;
            GameMvc.instance().model().get(LocalMap.class).normalizePosition(aspect.boxStart);
            // update render
        } else {
            commitSelection();
        }
    }

    /**
     * Clears selection box if it exists.
     */
    public void cancelSelection() {
        selector.getAspect(SelectorBoxAspect.class).boxStart = null;
        system.handleCancel();
    }

    /**
     * Commits selection box to {@link EntitySelectorSystem} for further handling.
     */
    public void commitSelection() {
        SelectorBoxAspect aspect = selector.getAspect(SelectorBoxAspect.class);
        if (aspect.boxStart == null) aspect.boxStart = selector.position;
        system.handleSelection();
        aspect.boxStart = null;
    }

    public void setSelectorPosition(Position position) {
        selector.position.set(position.x, position.y, selector.position.z);
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
    }

    public boolean moveByKey(int keycode) {
        int offset = Gdx.input.isKeyPressed(SHIFT_LEFT) ? 10 : 1;
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
                moveSelector(0, 0, 1);
                return true;
            case F:
                moveSelector(0, 0, -1);
        }
        return false;
    }

    public boolean secondaryMove(int keycode) {
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

    public void moveSelector(int dx, int dy, int dz) {
        cachePosition.set(selector.position);
        selector.position.add(dx, dy, dz);
        GameMvc.instance().model().get(LocalMap.class).normalizePosition(selector.position);
        if (!cachePosition.equals(selector.position)) system.selectorMoved();
    }
}
