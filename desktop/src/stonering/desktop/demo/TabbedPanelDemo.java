package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.desktop.sidebar.ScrollList;
import stonering.util.ui.SimpleScreen;

/**
 * @author Alexander on 25.12.2019.
 */
public class TabbedPanelDemo extends Game {
    Container fillContainer;
    ScrollList list;
    Container wrapContainer;
    Container wideContainer;

    public static void main(String[] args) {
        new LwjglApplication(new TabbedPanelDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private Stage stage;

            @Override
            public void show() {
                stage = new Stage();
                stage.addActor(createContainer());
                stage.setDebugAll(true);
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void resize(int width, int height) {
            }

            @Override
            public void render(float delta) {
                stage.act();
                stage.draw();
            }
        });
    }

    private Container createContainer() {
        fillContainer = new Container();

        return fillContainer;
    }
}
