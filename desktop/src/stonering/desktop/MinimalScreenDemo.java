package stonering.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * @author Alexander_Kuzyakov on 29.05.2019.
 */
public class MinimalScreenDemo extends ApplicationAdapter {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private Sprite image;
    private OrthographicCamera camera;

    public static void main(String[] args) {
        MinimalScreenDemo demo = new MinimalScreenDemo();
        new LwjglApplication(demo);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        image = new Sprite(new Texture("core/assets/sprites/map_tiles.png"));
        camera = new OrthographicCamera();
        updateCameraSize();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, 0, 0);
        batch.draw(image, 1000, 1000);
        batch.end();
        drawAxis();
    }

    private void drawAxis() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.line(0, -10000, 0, 10000);
        shapeRenderer.line(-10000, 0, 10000, 0);
        shapeRenderer.end();
    }

    private void updateCameraSize() {
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight= Gdx.graphics.getHeight();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        updateCameraSize();
    }
}
