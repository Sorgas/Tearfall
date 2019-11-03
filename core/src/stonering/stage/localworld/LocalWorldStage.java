package stonering.stage.localworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import stonering.stage.UiStage;
import stonering.stage.renderer.DrawingUtil;
import stonering.stage.renderer.EntitySelectorRenderer;
import stonering.stage.renderer.TileRenderer;

/**
 * Contains renderers for drawing local world and launches them in a sequence.
 * <p>
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, unit, and item are taken from LocalMap.
 * <p>
 * TODO move color from batch to sprites.
 * <p>
 * Only this component does zooming.
 *
 * @author Alexander Kuzyakov on 13.06.2017.21
 */
public class LocalWorldStage extends UiStage {
    private DrawingUtil drawingUtil;
    private TileRenderer tileRenderer;
    private EntitySelectorRenderer entitySelectorRenderer;
    private MovableCamera camera;

    public LocalWorldStage() {
        super();
        camera = new MovableCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawingUtil = new DrawingUtil(getBatch());
        tileRenderer = new TileRenderer(drawingUtil, camera);
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

    /**
     * This stage handles input directly. So zoom keys cannot be used anywhere else.
     */
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.PLUS)) {
            camera.zoom(0.1f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)) {
            camera.zoom(-0.1f);
        }
    }

    /**
     * Called in window resize. Resizes camera to update it's visible area.
     */
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.resize(width, height);
    }

    @Override
    public MovableCamera getCamera() {
        return camera;
    }
}