package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.util.ui.SimpleScreen;

/**
 * @author Alexander on 25.12.2019.
 */
public class TabbedPanelDemo extends Game {

    public static void main(String[] args) {
        new LwjglApplication(new TabbedPanelDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private Stage stage;

            @Override
            public void show() {
                stage = createStage();
            }

            @Override
            public void resize(int width, int height) {
                // resize stage
            }


        });
    }

    private Stage createStage( ){
        return new Stage();
    }
}
