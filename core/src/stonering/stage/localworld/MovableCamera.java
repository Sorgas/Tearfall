package stonering.stage.localworld;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.stage.renderer.BatchUtil;
import stonering.widget.util.Resizeable;
import stonering.util.geometry.Float2dBounds;
import stonering.util.geometry.Position;

import static stonering.stage.renderer.BatchUtil.*;
import static stonering.stage.renderer.BatchUtil.TILE_DEPTH;
import static stonering.stage.renderer.BatchUtil.TILE_WIDTH;

/**
 * {@link OrthographicCamera} extension.
 * Camera has:
 * additional position in {@link LocalMap} unit
 * visible area used by renderers * area, where {@link EntitySelector} can move without moving camera.
 * If selector moves out of it's area, camera is moved.
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
    private float[] zoomBounds = {0.5f, 4};
    private float[] screenSize;
    private static final Vector3 correctionVector = new Vector3(TILE_WIDTH / 2, TILE_DEPTH / 2, 0);
    private static final float CAMERA_SPEED = 0.3f;

    public MovableCamera(float viewportWidth, float viewportHeight) {
        super(viewportWidth, viewportHeight);
        screenSize = new float[2];
        frame = new Float2dBounds();
    }

    /**
     * Moves camera if {@link EntitySelector} is out of screen.
     */
    @Override
    public void update() {
        super.update();
        if (frame == null) {
            System.out.println("camera frame is null");
            return;
        }
        Vector3 vector = getOutOfFrameVector(GameMvc.model().get(EntitySelectorSystem.class).selector);
        if (vector.isZero()) return;
        position.add(vector.scl(CAMERA_SPEED));
        updateFrame();
    }

    /**
     * Updates camera and areas size.
     */
    @Override
    public void resize(int width, int height) {
        screenSize[0] = width;
        screenSize[1] = height;
        updateSize();
    }

    /**
     * Changes zoom value and ensures it is within bounds;
     */
    public void zoom(float delta) {
        zoom = Math.max(zoomBounds[0], Math.min(zoomBounds[1], zoom + delta));
        updateSize();
    }

    /**
     * Notifies camera about {@link EntitySelector} moves.
     * If selector moves by z-axis, camera changes position by z.
     */
    public void handleSelectorMove() {
        Position selectorPosition = GameMvc.model().get(EntitySelectorSystem.class).selector.position;
        int delta = selectorPosition.z - cameraZ;
        if (delta == 0) return;
        position.y += getBatchY(0, delta);
        cameraZ = selectorPosition.z;
        updateFrame();
    }

    /**
     * Checks that floor part of entity selector tiles are fully shown in frame.
     * Forms a vector to show direction to that tile.
     * Tile should be on the same z-level with camera.
     */
    private Vector3 getOutOfFrameVector(EntitySelector selector) {
        return frame.getOutVector(BatchUtil.getBottomLeftCorner(selector.position),
                BatchUtil.getRightTopCorner(selector.position.x + selector.size.x, selector.position.y + selector.size.y, selector.position.z));
    }

    /**
     * Sets camera position to given model position.
     */
    public void centerCameraToPosition(Position newPosition) {
        cameraZ = newPosition.z;
        position.x = getBatchX(newPosition.x);
        position.y = getBatchY(newPosition.y, newPosition.z);
        position.add(correctionVector);
        updateFrame();
    }

    /**
     * Updates visible frame of camera.
     */
    private void updateFrame() {
        frame.set(position.x - viewportWidth / 2 + 64,
                position.y - viewportHeight / 2 + 64,
                position.x + viewportWidth / 2 - 64,
                position.y + viewportHeight / 2 - 64);
    }

    /**
     * Updates size
     */
    private void updateSize() {
        viewportWidth = screenSize[0] * zoom;
        viewportHeight = screenSize[1] * zoom;
        updateFrame();
    }

    public Float2dBounds getFrame() {
        return frame;
    }

    public int getCameraZ() {
        return cameraZ;
    }
}
