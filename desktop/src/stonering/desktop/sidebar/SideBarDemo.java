package stonering.desktop.sidebar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.screen.SimpleScreen;
import stonering.stage.UiStage;

import java.util.Arrays;
import java.util.stream.Stream;

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
            private UiStage stage = new UiStage();

            @Override
            public void show() {
                Stream.of(Align.left ,Align.right, Align.top, Align.bottom).map(this::createContainer).forEach(stage::addActor);
                Gdx.input.setInputProcessor(stage);
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                stage.act(delta);
                stage.draw();
            }

            @Override
            public void resize(int width, int height) {
                stage.resize(width, height);
            }

            private Container createContainer(int align) {
                Container container = new Container<>(new Sidebar<>(new DemoWidget(), align, 0.8f));
                container.setFillParent(true);
                container.align(align);
                container.setDebug(true, true);
                return container;
            }
        });
    }
}
