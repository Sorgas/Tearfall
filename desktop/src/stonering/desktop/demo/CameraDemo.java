package stonering.desktop.demo;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import stonering.screen.SimpleScreen;
import stonering.util.global.StaticSkin;

/**
 * Demonstrates Camera work.
 *
 * @author Alexander on 19.02.2019.
 */
public class CameraDemo {

    public static void main(String[] args) {
        new LwjglApplication(new OrthographicCameraExample());
    }

    public static class OrthographicCameraExample extends Game {
        static final int WORLD_WIDTH = 100;
        static final int WORLD_HEIGHT = 100;
        Stage stage;

        private OrthographicCamera cam;
        private SpriteBatch batch;

        private Sprite mapSprite;
        private float rotationSpeed;
        private Container container;

        @Override
        public void create() {
            rotationSpeed = 0.5f;

            mapSprite = new Sprite(new Texture(Gdx.files.internal("sprites/plants.png")));
            mapSprite.setPosition(0, 0);
            mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

            float w = Gdx.graphics.getWidth();
            float h = Gdx.graphics.getHeight();
            stage = new Stage();
            stage.addActor(container = new Container());
            container.setActor(new TextButton("qwer", StaticSkin.getSkin()));
            container.right().fill();
            // Constructs a new OrthographicCamera, using the given viewport width and height
            // Height is multiplied by aspect ratio.
            cam = new OrthographicCamera(30, 30 * (h / w));

            cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
            cam.update();

            this.setScreen(new SimpleScreen() {
                @Override
                public void render(float delta) {
                    super.render(delta);
                    stage.draw();

                }
            });

            batch = new SpriteBatch();
        }

        @Override
        public void render() {
            handleInput();
            cam.update();
            batch.setProjectionMatrix(cam.combined);

            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            mapSprite.draw(batch);
            batch.end();

            super.render();
        }

        private void handleInput() {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                cam.zoom += 0.02;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
                cam.zoom -= 0.02;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                cam.translate(-3, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                cam.translate(3, 0, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                cam.translate(0, -3, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                cam.translate(0, 3, 0);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                cam.rotate(-rotationSpeed, 0, 0, 1);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.E)) {
                cam.rotate(rotationSpeed, 0, 0, 1);
            }

            cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100/cam.viewportWidth);

            float effectiveViewportWidth = cam.viewportWidth * cam.zoom;
            float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

            cam.position.x = MathUtils.clamp(cam.position.x, effectiveViewportWidth / 2f, 100 - effectiveViewportWidth / 2f);
            cam.position.y = MathUtils.clamp(cam.position.y, effectiveViewportHeight / 2f, 100 - effectiveViewportHeight / 2f);
        }

        @Override
        public void resize(int width, int height) {
            cam.viewportWidth = 30f;
            cam.viewportHeight = 30f * height/width;
            cam.update();
        }

        @Override
        public void resume() {
        }

        @Override
        public void dispose() {
            mapSprite.getTexture().dispose();
            batch.dispose();
        }

        @Override
        public void pause() {
        }
    }
}
