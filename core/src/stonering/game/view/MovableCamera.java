package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.stages.base.Resizeable;
import stonering.game.view.render.util.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import static stonering.game.view.render.stages.base.DrawingUtil.*;
import static stonering.game.view.render.stages.base.DrawingUtil.TILE_DEPTH;

/**
 * {@link OrthographicCamera} extension which have goal posiiton and speed.
 * Camera has additional position in {@link LocalMap} units, and visible area used by renderers.
 * Resizing window and zooming cause camera to update visible area.
 * {@link EntitySelector} move can cause camera to change position.
 *
 * @author Alexander_Kuzyakov on 03.06.2019.
 */
public class MovableCamera extends OrthographicCamera implements Resizeable {
    private Int3dBounds visibleArea; // inclusive ranges of fully visible tiles.
    private float zoom = 1;
    private float[] zoomBounds = {1, 3};
    private Position modelPosition;
    private static final Vector3 correctionVector = new Vector3(TILE_WIDTH / 2, TILE_DEPTH / 2, 0);

    public MovableCamera() {
        modelPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition().clone();
        visibleArea = new Int3dBounds();
        centerCameraToPosition(modelPosition);
        updateVisibleArea();
    }

    /**
     * Updates coordinate ranges of drawable tiles. Called on camera move and zoom, and window resize.
     * Area is counted basing on camera position, size and zoom.
     */
    private void updateVisibleArea() {
        LocalMap localMap = GameMvc.instance().getModel().get(LocalMap.class);
        int halfWidth = (int) Math.ceil(viewportWidth / 2 / TILE_WIDTH);
        int halfHeight = (int) Math.ceil(viewportHeight / 2 / TILE_DEPTH);
        visibleArea.set(
                Math.max(modelPosition.x - halfWidth, 0),
                Math.max(modelPosition.y - halfHeight, 0),
                modelPosition.z,
                Math.min(modelPosition.x + halfWidth, localMap.xSize - 1),
                Math.min(modelPosition.y + halfHeight, localMap.ySize - 1),
                modelPosition.z - 10);
    }

    @Override
    public void resize(int width, int height) {
        viewportWidth = width;
        viewportHeight = height;
        updateVisibleArea();
    }

    /**
     * Changes zoom value and ensures it is within bounds;
     */
    public void zoom(float delta) {
        zoom = Math.max(zoomBounds[0], Math.min(zoomBounds[1], zoom + delta));
        updateVisibleArea();
    }

    /**
     * Notifies camera about {@link EntitySelector} moves. If it moves out of visible area by x or y axis, camera changes position.
     */
    public void selectorMoved() {
        Position selectorPosition = GameMvc.instance().getModel().get(EntitySelector.class).getPosition();
        if (visibleArea.isIn(selectorPosition)) return;
        Vector2 offset = new Vector2();
        if (selectorPosition.x < visibleArea.getMinX()) offset.x = selectorPosition.x - visibleArea.getMinX();
        if (selectorPosition.x > visibleArea.getMaxX()) offset.x = selectorPosition.x - visibleArea.getMaxX();
        if (selectorPosition.y < visibleArea.getMinY()) offset.y = selectorPosition.y - visibleArea.getMinY();
        if (selectorPosition.y > visibleArea.getMaxY()) offset.y = selectorPosition.y - visibleArea.getMaxY();
        modelPosition.x += offset.x;
        modelPosition.y += offset.y;
        modelPosition.z = selectorPosition.z;
        centerCameraToPosition(selectorPosition);
        updateVisibleArea();
        TagLoggersEnum.UI.logDebug("Selector position: " + selectorPosition);
        TagLoggersEnum.UI.logDebug("Model position: " + modelPosition);
        TagLoggersEnum.UI.logDebug("Visible area+: " + visibleArea);
        TagLoggersEnum.UI.logDebug("Camera position: " + position.x + " " + position.y);
        System.out.println();
    }

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

    public Position getModelPosition() {
        return modelPosition;
    }
}
