package stonering.game.view;

import com.badlogic.gdx.graphics.OrthographicCamera;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.render.stages.base.DrawingUtil;
import stonering.game.view.render.stages.base.Resizeable;
import stonering.game.view.render.util.Int3DBounds;

/**
 * {@link OrthographicCamera} extension which have goal posiiton and speed.
 * Camera has additional position in {@link LocalMap} units, and visible area used by renderers.
 * Resizing window and zooming cause camera to update visible area.
 * {@link EntitySelector} move can cause camera to change position.
 *
 * @author Alexander_Kuzyakov on 03.06.2019.
 */
public class MovableCamera extends OrthographicCamera implements Resizeable {
    private Int3DBounds visibleArea; // inclusive ranges of fully visible tiles.
    private float zoom = 1;
    private float[] zoomBounds = {1, 3};

    public MovableCamera() {
        visibleArea = new Int3DBounds(0, 0, 0, 0, 0, 0);
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
    public void updateVisibleArea() {
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
}
