package stonering.desktop.demo;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;
import stonering.util.view.SimpleScreen;

/**
 * Demo with some UI elements.
 * TODO make tree with expanding on hover
 *
 * @author Alexander on 19.02.2019.
 */
public class LabelRotationDemo extends Game {
    private Table table;
    private List<Container> containers = new ArrayList<>();

    public static void main(String[] args) {
        new LwjglApplication(new LabelRotationDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                Container container = createContainer();
                stage.addActor(container);
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode != Input.Keys.ESCAPE) return false;
                        Gdx.app.exit();
                        return true;
                    }
                });

                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyTyped(InputEvent event, char character) {
                        containers.forEach(container1 -> {
                            container1.setOrigin(Align.center);
                            container1.rotateBy(90);
//                            System.out.println("size " + container1.getPrefWidth() + " " + container1.getPrefHeight());
                        });
                        table.invalidate();
                        table.layout();
                        System.out.println(table.getWidth() + " " + table.getHeight());
                        return true;
                    }
                });
                Gdx.input.setInputProcessor(stage);
                stage.setDebugAll(true);
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
        });
    }

    private Container createContainer() {
        Container container = new Container();
        table = new Table();
        for (int i = 0; i < 4; i++) {
            Label label = new Label("qwer12345", StaticSkin.skin());
            Container container1 = new Container();
            table.add(container1);
            container1.setTransform(true);
            container1.setOrigin(Align.center);
            container1.rotateBy(90);
            containers.add(container1);
            container1.setActor(label);
        }
        table.setTransform(true);
        container.setActor(table);
        container.setTransform(true);
        container.setFillParent(true);
        return container;
    }
}
