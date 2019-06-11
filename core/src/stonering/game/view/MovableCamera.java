package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.util.Resizeable;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

import static stonering.game.view.render.stages.renderer.DrawingUtil.*;

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
    private Int2dBounds selectorBounds; // inclusive ranges of allowed selector positions.
    int[] visibleAreaSize;
    int[] controlAreaSize;
    private float zoom = 1;
    private float[] zoomBounds = {1, 3};
    private static final Vector3 correctionVector = new Vector3(TILE_WIDTH / 2, TILE_DEPTH / 2, 0);

    public MovableCamera() {
        modelPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition().clone();
        visibleArea = new Int3dBounds();
        selectorBounds = new Int3dBounds();
        visibleAreaSize = new int[3];
        controlAreaSize = new int[2];
        centerCameraToPosition(modelPosition);
        updateAreas();
    }

    /**
     * Updates coordinate ranges of drawable tiles.
     * Updates selector allowed area.
     * Called on camera move and zoom, and window resize.
     * Area is counted basing on camera position, size and zoom.
     */
    private void updateAreas() {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        visibleArea.set(
                Math.max(modelPosition.x - visibleAreaSize[0], 0),
                Math.max(modelPosition.y - visibleAreaSize[1], 0),
                Math.max(modelPosition.z - visibleAreaSize[2], 0),
                Math.min(modelPosition.x + visibleAreaSize[0], localMap.xSize - 1),
                Math.min(modelPosition.y + visibleAreaSize[1], localMap.ySize - 1),
                Math.min(modelPosition.z, localMap.zSize - 1));
        selectorBounds.set(
                Math.max(modelPosition.x - controlAreaSize[0], 0),
                Math.max(modelPosition.y - controlAreaSize[1], 0),
                Math.min(modelPosition.x + controlAreaSize[0], localMap.xSize - 1),
                Math.min(modelPosition.y + controlAreaSize[1], localMap.ySize - 1));
    }

    /**
     * Updates camera and areas size.
     * Selector bounds are always more narrow than visible area, so selector tile is fully visible.
     */
    @Override
    public void resize(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;
        visibleAreaSize[0] = (int) Math.ceil((viewportWidth - TILE_WIDTH) / 2 / TILE_WIDTH);
        visibleAreaSize[1] = (int) Math.ceil((viewportHeight - TILE_DEPTH) / 2 / TILE_DEPTH);
        visibleAreaSize[2] = 15; //TODO number of z levels should depend on screen height
        controlAreaSize[0] = visibleAreaSize[0] - 1;
        controlAreaSize[1] = visibleAreaSize[1] - 1;
        selectorMoved();
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
        if (selectorBounds.isIn(selectorPosition) && selectorPosition.z == modelPosition.z)
            return; // no actions, if selector moves within it's bounds;
        modelPosition.z = selectorPosition.z;
        if (!selectorBounds.isIn(selectorPosition)) {
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
     * Notifies camera about {@link EntitySelector} moves.
     * If selector moves by z-axis, camera changes position by z.
     * If it moves out of visible area by x or y axis, camera changes position.
     */
    private void selectorMove() {
        Position selectorPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition();
        Vector2 vector = getOutOfFrameVector(selectorPosition);
        if (modelPosition.z == selectorPosition.z && vector.isZero()) return;
        modelPosition.z = selectorPosition.z;
        modelPosition.x += vector.x;
        modelPosition.y += vector.y;
        updateAreas();
    }

    /**
     * Checks that floor part of tile in given position is fully shown in frame.
     * Forms a vector to show direction to that tile.
     * Tile should be on the same z-level with camera.
     */
    private Vector2 getOutOfFrameVector(Position position) {
        Vector2 vector = new Vector2();
        if(this.position.x - viewportWidth / 2 < position.x * TILE_WIDTH) vector.x = -1;
        if(this.position.x + viewportWidth / 2 > (position.x + 1) * TILE_WIDTH - 1) vector.x = 1;
        if(this.position.y - viewportHeight / 2 < position.y * TILE_DEPTH) vector.y = -1;
        if(this.position.y + viewportHeight / 2 < (position.y + 1) * TILE_DEPTH - 1) vector.y = 1;
        return vector;
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
