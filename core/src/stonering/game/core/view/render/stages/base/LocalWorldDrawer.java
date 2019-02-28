package stonering.game.core.view.render.stages.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.model.GameModel;

/**
 * Contains renderers for drawing local world and launches them in a sequence.
 *
 * Draws LocalMap. Blocks and plants are taken from LocalTileMap,
 * Buildings, units, and items are taken from LocalMap.
 *
 * TODO move color from batch to sprites.
 *
 * @author Alexander Kuzyakov on 13.06.2017.
 */
public class LocalWorldDrawer extends UiStage {
    private DrawingUtil drawingUtil;
    private TileRenderer tileRenderer;
    private EntitySelectorRenderer entitySelectorRenderer;
    private OrthographicCamera camera;

    public LocalWorldDrawer(GameModel gameModel) {
        super();
        camera = (OrthographicCamera) getCamera();
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        drawingUtil = new DrawingUtil(this.getBatch());
        tileRenderer = new TileRenderer(gameModel, drawingUtil);
        entitySelectorRenderer = new EntitySelectorRenderer(gameModel, drawingUtil);
    }

    /**
     * Renders local map with all entities to single batch frame.
     */
    public void draw() {
        handleInput();
        getBatch().setProjectionMatrix(camera.combined);
        camera.update();
        drawingUtil.begin();
        tileRenderer.render();
        entitySelectorRenderer.render();
        drawingUtil.end();
    }

//    public Vector2 translateScreenPositionToModel(Vector2 screenPos) {
//        Vector2 vector = screenPos.sub(new Vector2(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2)); // to click point from center
//        vector.set(vector.x / tileWidth, -vector.y / tileDepth);
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
        getCamera().position.set(width /2f + 32, height/2f + 32,0);
    }

    public void zoom(float delta) {
        camera.zoom +=delta;
        camera.zoom = MathUtils.clamp(camera.zoom, 0.5f, 2f);
    }
}