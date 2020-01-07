package stonering.desktop;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import java.nio.IntBuffer;

public class Main implements ApplicationListener {

    public static void main(String[] args) {
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "atmos";
        cfg.width = 480;
        cfg.height = 320;
        LwjglApplication app = new LwjglApplication(new Main(), cfg);
    }

    public static void setHWCursorVisible(boolean visible) throws LWJGLException {
        if (Gdx.app.getType() != Application.ApplicationType.Desktop && Gdx.app instanceof LwjglApplication) return;
        if (emptyCursor == null) {
            if (Mouse.isCreated()) {
                int min = org.lwjgl.input.Cursor.getMinCursorSize();
                IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
                emptyCursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
            } else {
                throw new LWJGLException("Could not create empty cursor before Mouse object is created");
            }
        }
        if (Mouse.isInsideWindow()) Mouse.setNativeCursor(visible ? null : emptyCursor);
    }

    SpriteBatch batch;
    OrthographicCamera camera;
    static org.lwjgl.input.Cursor emptyCursor;
    boolean hwVisible = false;
    Texture cursor;
    int xHotspot, yHotspot;


    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();

        cursor = new Texture(Gdx.files.internal("sprites/mouse/regular.png"));
        xHotspot = 0;
        yHotspot = cursor.getHeight(); //lower left origin!


        //press SPACE to toggle software cursor
        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean keyDown(int key) {
                if (key == Input.Keys.SPACE) {
                    hwVisible = !hwVisible;
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);

        try {
            setHWCursorVisible(hwVisible);
        } catch (LWJGLException e) {
            throw new GdxRuntimeException(e);
        }


        batch.begin();

        //... draw sprites ...

        //draw SW cursor
        if (!hwVisible) {
            int x = Gdx.input.getX();
            int y = Gdx.input.getY();
            batch.draw(cursor, x - xHotspot, Gdx.graphics.getHeight() - y - 1 - yHotspot);
        }
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        cursor.dispose();
        batch.dispose();
    }
}
