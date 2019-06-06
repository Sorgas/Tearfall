package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.stages.base.Resizeable;
import stonering.game.view.render.util.Int3dBounds;
import stonering.util.geometry.Position;

import static stonering.game.view.render.stages.base.DrawingUtil.*;
import static stonering.game.view.render.stages.base.DrawingUtil.TILE_DEPTH;

/**
 * {@link OrthographicCamera} extension.
 * Camera has:
 * additional position in {@link LocalMap} units
 * visible area used by renderers
 * area, where {@link EntitySelector} can move without moving camera.
 * If selector moves out of it's area, then camera is moved.
 * When camera moves, both areas are updated.
 * Camera always moves when selector moves in z axis.
 * Resizing window and zooming cause camera to update both areas, without changing camera position.
 *
 * @author Alexander_Kuzyakov on 03.06.2019.
 */
public class MovableCamera extends OrthographicCamera implements Resizeable {
    private Position modelPosition;
    private Int3dBounds visibleArea; // inclusive ranges of fully visible tiles.
    private Int3dBounds selectorBounds; // inclusive ranges of allowed selector positions.
    private float zoom = 1;
    private float[] zoomBounds = {1, 3};
    private static final Vector3 correctionVector = new Vector3(TILE_WIDTH / 2, TILE_DEPTH / 2, 0);

    public MovableCamera() {
        modelPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition().clone();
        visibleArea = new Int3dBounds();
        selectorBounds = new Int3dBounds();
        centerCameraToPosition(modelPosition);
        updateAreas();
    }

    /**
     * Updates coordinate ranges of drawable tiles.
     * Updates selector allowed area.
     * Selector bounds are always more narrow than visible area, so selector tile is fully visible.
     * Called on camera move and zoom, and window resize.
     * Area is counted basing on camera position, size and zoom.
     */
    private void updateAreas() {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        int halfWidth = (int) Math.ceil(viewportWidth / 2 / TILE_WIDTH);
        int halfHeight = (int) Math.ceil(viewportHeight / 2 / TILE_DEPTH);
        visibleArea.set(
                Math.max(modelPosition.x - halfWidth, 0),
                Math.max(modelPosition.y - halfHeight, 0),
                Math.max(modelPosition.z - 10, 0),
                Math.min(modelPosition.x + halfWidth, localMap.xSize - 1),
                Math.min(modelPosition.y + halfHeight, localMap.ySize - 1),
                Math.min(modelPosition.z, localMap.zSize - 1));
        selectorBounds.set(
                Math.max(visibleArea.getMinX() + 1, 0),
                Math.max(visibleArea.getMinY() + 1, 0),
                modelPosition.z,
                Math.min(visibleArea.getMaxX(), localMap.xSize) - 1,
                Math.min(visibleArea.getMaxY(), localMap.xSize) - 1,
                modelPosition.z);
    }

    @Override
    public void resize(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;
        updateAreas();
    }

    /**
     * Changes zoom value and ensures it is within bounds;
     */
    public void zoom(float delta) {
        zoom = Math.max(zoomBounds[0], Math.min(zoomBounds[1], zoom + delta));
        updateAreas();
    }

    /**
     * Notifies camera about {@link EntitySelector} moves. If it moves out of visible area by x or y axis, camera changes position.
     */
    public void selectorMoved() {
        Position selectorPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition();
        // no actions, if selector moves within it's bounds;
        if (selectorBounds.isIn(selectorPosition)) return;
        modelPosition.z = selectorPosition.z;
        if (!selectorBounds.isInByXY(selectorPosition)) {
            Vector2 offset = new Vector2();
            if (selectorPosition.x < selectorBounds.getMinX()) offset.x = selectorPosition.x - selectorBounds.getMinX();
            if (selectorPosition.x > selectorBounds.getMaxX()) offset.x = selectorPosition.x - selectorBounds.getMaxX();
            if (selectorPosition.y < selectorBounds.getMinY()) offset.y = selectorPosition.y - selectorBounds.getMinY();
            if (selectorPosition.y > selectorBounds.getMaxY()) offset.y = selectorPosition.y - selectorBounds.getMaxY();
            modelPosition.x += offset.x;
            modelPosition.y += offset.y;
        }
        centerCameraToPosition(modelPosition);
        updateAreas();
    }

    /**
     * Sets camera position to given model position.
     */
    private void centerCameraToPosition(Position newPosition) {
        position.x = modelXtoBatchX(newPosition.x);
        position.y = modelYZtoBatchY(newPosition.y, newPosition.z);
        position.add(correctionVector);
    }

    private int modelXtoBatchX(int x) {
        return x * TILE_WIDTH;
    }

    private int modelYZtoBatchY(int y, int z) {
        return y * TILE_DEPTH + z * (TILE_HEIGHT - TILE_DEPTH);
    }

    public Int3dBounds getVisibleArea() {
        return visibleArea;
    }
}
