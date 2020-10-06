package stonering.stage.localworld;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import stonering.stage.util.UiStage;
import stonering.stage.renderer.Drawer;
import stonering.stage.renderer.ShapeDrawingUtil;
import stonering.stage.renderer.SpriteDrawingUtil;
import stonering.stage.renderer.EntitySelectorDrawer;
import stonering.stage.renderer.TileDrawer;

/**
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, unit, and item are taken from LocalMap.
 * Contains {@link TileDrawer} for drawing local world and launches them in a sequence.
 * Zooming is made with {@link MovableCamera}.
 * Has util classes for drawing sprites and shapes. Sprite and shape batches updated with camera projection matrix.
 *
 * @author Alexander Kuzyakov on 13.06.2017.21
 */
public class LocalWorldStage extends UiStage {
    private MovableCamera camera;
    private TileDrawer tileDrawer;
    //TODO zone renderer.

    private final SpriteDrawingUtil spriteDrawingUtil;
    private final ShapeDrawingUtil shapeDrawingUtil;

    public LocalWorldStage() {
        super();
        camera = new MovableCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteDrawingUtil = new SpriteDrawingUtil(getBatch());
        shapeDrawingUtil = new ShapeDrawingUtil(getBatch());
        tileDrawer = new TileDrawer(spriteDrawingUtil, shapeDrawingUtil, camera);
    }
    
    /**
     * Renders local map with all entities to single batch frame.
     */
    public void draw() {
        handleInput();
        camera.update();
        getBatch().setProjectionMatrix(camera.combined);
        shapeDrawingUtil.shapeRenderer.setProjectionMatrix(camera.combined);
        getBatch().begin();
        tileDrawer.render();
        getBatch().end();
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

    public void resize(int width, int height) {
        super.resize(width, height);
        camera.resize(width, height);
    }

    @Override
    public MovableCamera getCamera() {
        return camera;
    }
}