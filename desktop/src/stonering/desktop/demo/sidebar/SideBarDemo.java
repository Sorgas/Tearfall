package stonering.desktop.demo.sidebar;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import stonering.desktop.sidebar.ScrollList;
import stonering.screen.util.SimpleScreen;
import stonering.stage.util.UiStage;
import stonering.util.lang.StaticSkin;

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
            private Label label;
            private AutoClosingSidebar<ScrollList> sidebar;

            @Override
            public void show() {
                UiStage stage2 = new UiStage();
                stage2.interceptInput = false;
                label = new Label("", StaticSkin.getSkin());
                Container container = new Container<>(label);
                container.align(Align.right).size(300, 300).setFillParent(true);
                stage2.addActor(container);
                stages = Stream.of(Align.right, Align.top, Align.bottom, Align.left)
                        .map(this::wrapWithContainer)
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
                    float current = sidebar.pane.getScrollX();
                    float toClose = current - sidebar.closedScrollValue;
                    float toOpen = current - sidebar.openedScrollValue;
                    label.setText("to close: " + toClose+ " \n" +
                            "to open:" + toOpen+ " \n"+
                            "current: " + current);
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

            private Container<AutoClosingSidebar<ScrollList>> wrapWithContainer(int align) {
                sidebar = new AutoClosingSidebar<>(new ScrollList(align), align, 0.8f);
                Container<AutoClosingSidebar<ScrollList>> container = new Container<>(sidebar);
                container.setFillParent(true);
                container.align(align);
                container.fillY();
                container.setDebug(true, true);
                return container;
            }
        });
    }
}
