package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.stages.base.DrawingUtil;
import stonering.game.view.render.stages.base.Resizeable;
import stonering.game.view.render.util.Float3DBounds;
import stonering.util.geometry.Position;

/**
 * {@link OrthographicCamera} extension which have goal posiiton and speed.
 * Camera has additional position in {@link LocalMap} units, and visible area used by renderers.
 * Resizing window and zooming cause camera to update visible area.
 * {@link EntitySelector} move can cause camera to change position.
 *
 * @author Alexander_Kuzyakov on 03.06.2019.
 */
public class MovableCamera extends OrthographicCamera implements Resizeable {
    private Float3DBounds visibleArea; // inclusive ranges of fully visible tiles.
    private float zoom = 1;
    private float[] zoomBounds = {1, 3};

    public MovableCamera() {
        visibleArea = new Float3DBounds();
        updateVisibleArea();
    }

    @Override
    public void update() {
        super.update();

    }

    /**
     * Updates coordinate ranges of drawable tiles. Called on camera move and zoom, and window resize.
     * Area is counted basing on camera position, size and zoom.
     */
    private void updateVisibleArea() {
        GameModel gameModel = GameMvc.instance().getModel();
        LocalMap localMap = gameModel.get(LocalMap.class);
        EntitySelector selector = gameModel.get(EntitySelector.class);
        visibleArea.set(
                (int) Math.max((position.x - viewportWidth / 2) / DrawingUtil.TILE_WIDTH, 0),
                (int) Math.max((position.y - viewportHeight / 2) / DrawingUtil.TILE_DEPTH, 0),
                (int) Math.max((viewportHeight / 2) / (DrawingUtil.TILE_HEIGHT - DrawingUtil.TILE_DEPTH), 0),
                (int) Math.min((position.x + viewportWidth / 2) / DrawingUtil.TILE_WIDTH, localMap.xSize - 1),
                (int) Math.min((position.y + viewportHeight / 2) / DrawingUtil.TILE_DEPTH, localMap.ySize - 1),
                selector.getPosition().z);
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
        Vector3 offset = new Vector3();
        if (selectorPosition.x < visibleArea.getMinX()) offset.x = -DrawingUtil.TILE_WIDTH;
        if (selectorPosition.x > visibleArea.getMaxX()) offset.x = DrawingUtil.TILE_WIDTH;
        if (selectorPosition.y < visibleArea.getMinY()) offset.y = -DrawingUtil.TILE_DEPTH;
        if (selectorPosition.y > visibleArea.getMaxY()) offset.y = DrawingUtil.TILE_DEPTH;
        position.add(offset);
        updateVisibleArea();
    }

    public Float3DBounds getVisibleArea() {
        return visibleArea;
    }
}
