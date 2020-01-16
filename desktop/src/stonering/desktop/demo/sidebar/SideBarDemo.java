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
import stonering.desktop.sidebar.ScrollList;
import stonering.util.ui.SimpleScreen;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;

import java.util.List;
import java.util.stream.Collectors;
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
            private List<UiStage> stages;

            @Override
            public void show() {
                UiStage stage2 = new UiStage();
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
                stages = Stream.of(Align.right, Align.left, Align.bottom, Align.top)
                        .map(this::createContainer)
                        .map(container1 -> {
                                    UiStage stageq = new UiStage();
                                    stageq.interceptInput = false;
                                    stageq.addActor(container1);
                                    return stageq;
                                }
                        ).collect(Collectors.toList());
                stages.add(stage2);
                Gdx.input.setInputProcessor(new InputMultiplexer(stages.toArray(new UiStage[]{})));
            }

            @Override
            public void render(float delta) {
                Gdx.gl.glClearColor(0, 0, 0, 1);
                Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
                for (int i = stages.size() - 1; i >= 0; i--) {
                    UiStage stage = stages.get(i);
                    stage.act();
                    stage.draw();
                }
            }

            @Override
            public void resize(int width, int height) {
                for (UiStage stage : stages) {
                    stage.resize(width, height);
                }
            }

            private Container createContainer(int align) {
                Container container = new Container(new AutoClosingSidebar<>(new ScrollList(align), align, 0.8f));
                container.setFillParent(true);
                container.align(align);
                container.fillY();
                container.setDebug(true, true);
                return container;
            }
        });
    }
}
