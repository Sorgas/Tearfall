package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.stages.renderer.BatchUtil;
import stonering.game.view.render.util.Resizeable;
import stonering.util.geometry.Float2dBounds;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import static stonering.game.view.render.stages.renderer.BatchUtil.*;
import static stonering.game.view.render.stages.renderer.BatchUtil.TILE_DEPTH;
import static stonering.game.view.render.stages.renderer.BatchUtil.TILE_WIDTH;

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
    private int cameraZ;
    private Float2dBounds frame; // visible batch area
    private float zoom = 1;
    private float[] zoomBounds = {1, 3};
    private static final Vector3 correctionVector = new Vector3(TILE_WIDTH / 2, TILE_DEPTH / 2, 0);

    public MovableCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
        Logger.UI.logDebug("camera constructor");
        frame = new Float2dBounds();
        centerCameraToPosition(GameMvc.instance().getModel().get(EntitySelector.class).getPosition().clone());
        updateFrame();
    }

    /**
     * Moves camera to goal, if {@link EntitySelector} is out of screen.
     */
    @Override
    public void update() {
        super.update();
    }

    /**
     * Updates camera and areas size.
     * Selector bounds are always more narrow than visible area, so selector tile is fully visible.
     */
    @Override
    public void resize(int width, int height) {
        Logger.UI.logDebug("camera resize");
        viewportWidth = width;
        viewportHeight = height;
        updateFrame();
    }

    /**
     * Changes zoom value and ensures it is within bounds;
     */
    public void zoom(float delta) {
        Logger.UI.logDebug("camera zoom");
        zoom = Math.max(zoomBounds[0], Math.min(zoomBounds[1], zoom + delta));
        updateFrame();
    }

    /**
     * Notifies camera about {@link EntitySelector} moves.
     * If selector moves by z-axis, camera changes position by z.
     * If it moves out of visible area by x or y axis, camera changes position.
     */
    public void selectorMoved() {
        Position selectorPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition();
        int delta = selectorPosition.z - cameraZ;
        if (delta != 0) {
            position.y += getBatchY(0, delta);
            cameraZ += selectorPosition.z;
            updateFrame();
        }
        Vector2 vector = getOutOfFrameVector(selectorPosition);
        if (vector.isZero()) return;
        position.x += vector.x;
        position.y += vector.y;
        updateFrame();
    }

    /**
     * Checks that floor part of tile in given position is fully shown in frame.
     * Forms a vector to show direction to that tile.
     * Tile should be on the same z-level with camera.
     */
    private Vector2 getOutOfFrameVector(Position position) {
        return frame.getOutVector(BatchUtil.getBottomLeftCorner(position), BatchUtil.getRightTopCorner(position));
    }

    /**
     * Sets camera position to given model position.
     */
    public void centerCameraToPosition(Position newPosition) {
        cameraZ = newPosition.z;
        position.x = getBatchX(newPosition.x);
        position.y = getBatchY(newPosition.y, newPosition.z);
        position.add(correctionVector);
    }

    /**
     * Updates visible frame of camera.
     */
    private void updateFrame() {
        frame.set(position.x - viewportWidth / 2,
                position.y - viewportHeight / 2,
                position.x + viewportWidth / 2,
                position.y + viewportHeight / 2);
    }

    public Float2dBounds getFrame() {
        return frame;
    }

    public int getCameraZ() {
        return cameraZ;
    }
}
