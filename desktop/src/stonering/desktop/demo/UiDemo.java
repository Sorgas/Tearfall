package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;

import stonering.stage.util.UiStage;
import stonering.util.global.StaticSkin;
import stonering.screen.util.SimpleScreen;

/**
 * Demo with some UI elements.
 * TODO make tree with expanding on hover
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {
    private Container container2;
    
    public static void main(String[] args) {
        new LwjglApplication(new UiDemo());
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
                        if (keycode == Input.Keys.ESCAPE) {
                            Gdx.app.exit();
                        } else {
                            System.out.println(container.getWidth());
                            container.width(container.getWidth() + 20);
                        }
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
        Table table = new Table();
        table.add(new Label("qwer1", StaticSkin.skin())).size(100, 100).row();
        table.add().expandY().row();
        table.add(new Label("qwer2", StaticSkin.skin())).size(100, 100);
        Container container = new Container(table);
        container.setFillParent(true);
        container.fill();
        container.setDebug(true, true);
        return container;
    }
}
