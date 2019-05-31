package stonering.desktop;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * @author Alexander_Kuzyakov on 29.05.2019.
 */
public class MinimalScreenDemo extends ApplicationAdapter {
    private SpriteBatch batch;
    private Sprite image;
    private OrthographicCamera camera;
    private float zoom = 1;
    private float[] zoomBounds = {0.5f, 10f};

    public static void main(String[] args) {
        MinimalScreenDemo demo = new MinimalScreenDemo();
        new LwjglApplication(demo);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Sprite(new Texture("core/assets/sprites/blocks.png"));
        camera = new OrthographicCamera();
        updateCameraSize();
        camera.position.set(500,100,0);
        Gdx.input.setInputProcessor(new InpunHandler(this));
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(image, 100, 100);
        batch.draw(image, 1000, 1000);
        batch.end();
    }

    private void updateCameraSize() {
        camera.viewportWidth = Gdx.graphics.getWidth() * zoom;
        camera.viewportHeight= Gdx.graphics.getHeight() * zoom;
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        updateCameraSize();
    }

    private static class InpunHandler extends InputAdapter {
        private MinimalScreenDemo demo;

        public InpunHandler(MinimalScreenDemo demo) {
            this.demo = demo;
        }

        @Override
        public boolean keyTyped(char character) {
            switch (character) {
                case 'w':
                    return move(0, 1);
                case 'a':
                    return move(-1, 0);
                case 's':
                    return move(0, -1);
                case 'd':
                    return move(1, 0);
                case 'r':
                    return zoom(0.05f);
                case 'f':
                    return zoom(-0.05f);
            }
            return false;
        }

        private boolean move(int x, int y) {
            demo.camera.translate(x * 5f * demo.zoom, y * 5f * demo.zoom);
            return true;
        }

        private boolean zoom(float d) {
            demo.zoom = Math.max(Math.min(demo.zoomBounds[1], demo.zoom + d), demo.zoomBounds[0]);
            demo.updateCameraSize();
            return true;
        }
    }
}
