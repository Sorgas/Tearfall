package stonering.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;

import stonering.screen.util.SimpleScreen;
import stonering.util.lang.Initable;
import stonering.util.logging.Logger;
import stonering.widget.util.Resizeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen that can contain and draw many stages.
 * Can add and remove them.
 * Stages with lesser indexes are rendered lower.
 *
 * @author Alexander on 25.12.2019.
 */
public class MultiStageScreen extends SimpleScreen {
    protected List<Stage> stageList = new ArrayList<>();
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stageList.forEach(stage -> {
            stage.act(delta);
            stage.draw();
        });
    }

    @Override
    public void resize(int width, int height) {
        stageList.stream().filter(stage -> stage instanceof Resizeable).forEach(stage -> ((Resizeable) stage).resize(width, height));
    }

    @Override
    public void dispose() {
        stageList.forEach(Stage::dispose);
    }

    public void addStage(Stage stage) {
        stageList.add(stage);
        if (stage instanceof Resizeable) ((Resizeable) stage).resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (stage instanceof Initable) ((Initable) stage).init(); // keyboard focus cannot be set before adding to screen
    }

    public void removeStage(Stage stage) {
        if(stage == null) {
            Logger.UI.logError("Attempt to remove null stage.");
            return;
        }
        stageList.remove(stage);
        stage.dispose();
    }
}
