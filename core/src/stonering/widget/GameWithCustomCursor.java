package stonering.widget;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;

import java.nio.IntBuffer;

/**
 * Game that renders additional batch with {@link}
 *
 * @author Alexander on 07.01.2020
 */
public class GameWithCustomCursor extends Game {
    private Batch batch;
    private OrthographicCamera camera;
    static org.lwjgl.input.Cursor emptyCursor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        if (Mouse.isCreated()) {
            int min = org.lwjgl.input.Cursor.getMinCursorSize();
            IntBuffer tmp = BufferUtils.createIntBuffer(min * min);
            try {
                emptyCursor = new org.lwjgl.input.Cursor(min, min, min / 2, min / 2, 1, tmp, null);
            } catch (LWJGLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        super.render();
        updateCursor();
        camera.update();
        batch.begin();
        if(Mouse.isInsideWindow()) MouseCursor.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        camera.setToOrtho(false, width, height);
        batch.setProjectionMatrix(camera.combined);
    }

    private void updateCursor() {
        try {
            if (Mouse.isInsideWindow()) Mouse.setNativeCursor(emptyCursor);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }
}
