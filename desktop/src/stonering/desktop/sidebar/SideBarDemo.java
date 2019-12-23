package stonering.desktop.sidebar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.screen.SimpleScreen;

/**
 * Demo game with sidebar.
 *
 * @author Alexander on 23.12.2019.
 */
public class SideBarDemo extends Game {

    public static void main(String[] args) {
        new LwjglApplication(new SideBarDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private Stage stage;

            {
                stage = new Stage();
                stage.addActor(createContainer());
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                stage.draw();
                stage.act(delta);
                super.render(delta);
            }
        });
    }

    private Container createContainer() {
        Container container = new Container<>(new Sidebar(new DemoWidget()));
        container.setFillParent(true);
        container.align(Align.right);
        container.setDebug(true, true);
        return container;
    }
}
