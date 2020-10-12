package stonering.screen.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import stonering.widget.util.Resizeable;

/**
 * Screen with stage.
 *
 * @author Alexander on 18.09.2019.
 */
public class SingleStageScreen extends SimpleScreen {
    protected Stage stage;

    public SingleStageScreen(Stage stage) {
        this.stage = stage;
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        if (stage == null) return;
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        if (stage != null && stage instanceof Resizeable) ((Resizeable) stage).resize(width, height);
    }
}
