package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import stonering.util.ui.SimpleScreen;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;

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
            private UiStage stage1 = new UiStage();
            private UiStage stage2 = new UiStage();

            @Override
            public void show() {
                stage1.interceptInput = false;
                stage2.interceptInput = false;
                TextButton button = new TextButton("qwer", StaticSkin.getSkin());
                button.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        System.out.println("qwer");
                    }
                });
                Container container = new Container<>(button);
                container.align(Align.right).size(300, 300).setFillParent(true);
                stage2.addActor(container);
                Stream.of(Align.left, Align.right, Align.top, Align.bottom)
                        .map(this::createContainer)
                        .forEach(stage1::addActor);
                Gdx.input.setInputProcessor(new InputMultiplexer(stage1, stage2));
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                stage2.act(delta);
                stage2.draw();
                stage1.act(delta);
                stage1.draw();
            }

            @Override
            public void resize(int width, int height) {
                stage1.resize(width, height);
                stage2.resize(width, height);
            }

            private Container createContainer(int align) {
                Container container = new Container<>(new Sidebar<>(new ScrollList(), align, 0.8f));
                container.setFillParent(true);
                container.align(align);
                container.setDebug(true, true);
                return container;
            }
        });
    }
}
