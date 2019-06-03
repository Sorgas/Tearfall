package stonering.game.view.render.stages.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import stonering.game.GameMvc;
import stonering.game.model.EntitySelector;
import stonering.game.model.GameModel;
import stonering.game.model.local_map.LocalMap;
import stonering.game.view.MovableCamera;
import stonering.game.view.render.util.Int3DBounds;

/**
 * Contains renderers for drawing local world and launches them in a sequence.
 * <p>
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, units, and items are taken from LocalMap.
 * <p>
 * TODO move color from batch to sprites.
 * <p>
 * Only this component does zooming.
 *
 * @author Alexander Kuzyakov on 13.06.2017.
 */
public class LocalWorldStage extends UiStage {
    private DrawingUtil drawingUtil;
    private TileRenderer tileRenderer;
    private EntitySelectorRenderer entitySelectorRenderer;
    private MovableCamera camera;
    private Int3DBounds visibleArea;

    public LocalWorldStage() {
        super();
        camera = new MovableCamera();
        camera.update();
        visibleArea = updateVisibleArea();
        drawingUtil = new DrawingUtil(this.getBatch());
        tileRenderer = new TileRenderer(drawingUtil, visibleArea);
        entitySelectorRenderer = new EntitySelectorRenderer(drawingUtil);
    }

    /**
     * Renders local map with all entities to single batch frame.
     */
    public void draw() {
        handleInput();
        camera.update();
        getBatch().setProjectionMatrix(camera.combined);
        drawingUtil.begin();
        tileRenderer.render();
        entitySelectorRenderer.render();
        drawingUtil.end();
    }

//    public Vector2 translateScreenPositionToModel(Vector2 screenPos) {
//        Vector2 vector = screenPos.sub(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2)); // to click point from center
//        vector.set(vector.x / TILE_WIDTH, -vector.y / TILE_DEPTH);
//        return new Vector2((float) Math.floor(selector.getPosition().getX() + vector.x), (float) Math.floor(selector.getPosition().getY() + vector.y));
//    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            zoom(0.1f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            zoom(-0.1f);
        }
    }

    /**
     * Called in window resize.
     */
    public void resize(int width, int height) {
        super.resize(width, height);
        getCamera().position.set(width / 2f + 32, height / 2f + 32, 0);
        updateVisibleArea();
    }

    public void zoom(float delta) {
        camera.zoom += delta;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);
        updateVisibleArea();
    }

    /**
     * Updates coordinate ranges of drawable draw. Called on camera move and zoom, and window resize.
     * Area is counted basing on camera position, size and zoom.
     */
    public Int3DBounds updateVisibleArea() {
        GameModel gameModel = GameMvc.instance().getModel();
        EntitySelector selector = gameModel.get(EntitySelector.class);
        LocalMap localMap = gameModel.get(LocalMap.class);
        int widthInTiles = Math.round(Gdx.graphics.getWidth() / 2f / (camera.zoom * DrawingUtil.TILE_WIDTH)) + 1;
        int depthInTiles = Math.round(Gdx.graphics.getHeight() / (camera.zoom * DrawingUtil.TILE_DEPTH)) + 1;
        if (visibleArea == null) visibleArea = new Int3DBounds(0, 0, 0, 0, 0, 0);
        visibleArea.set(
                Math.max(selector.getPosition().x - widthInTiles, 0),
                Math.max(selector.getPosition().y - widthInTiles, 0),
                Math.max(selector.getPosition().z - depthInTiles, 0),
                Math.min(selector.getPosition().x + widthInTiles, localMap.xSize - 1),
                Math.min(selector.getPosition().y + widthInTiles, localMap.ySize - 1),
                selector.getPosition().z);
        return visibleArea;
    }
}