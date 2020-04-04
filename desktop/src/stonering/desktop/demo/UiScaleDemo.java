package stonering.desktop.demo;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;

/**
 * @author Alexander on 03.04.2020
 */
public class UiScaleDemo extends Game{
    private float scale = 1f;

    public static void main(String[] args) {
        new LwjglApplication(new stonering.desktop.demo.UiScaleDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            final UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                stage.addActor(createContainer(Align.bottomRight));
                stage.addActor(createContainer(Align.topLeft));
                Gdx.input.setInputProcessor(stage);
                stage.setDebugAll(true);

                InputAdapter scaleAdapter = new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        if(keycode == Input.Keys.Q) {
                            scale += 0.25f;
                            stage.setUiScale(scale);
                            System.out.println("+" + scale);
                            return true;
                        } else if(keycode == Input.Keys.W) {
                            scale -= 0.25f;
                            stage.setUiScale(scale);
                            System.out.println("-" + scale);
                            return true;
                        }
                        return super.keyDown(keycode);
                    }
                };
                Gdx.input.setInputProcessor(new InputMultiplexer(scaleAdapter, stage));
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

    private Container createContainer(int align) {
        Table table = new Table();
        table.defaults().grow();
        table.add(new TextButton("qwer", StaticSkin.getSkin()));
        table.add(new Label("qwer", StaticSkin.getSkin())).row();
        table.add(new CheckBox("check", StaticSkin.getSkin()));
        Container<Table> container = new Container<>(table);
        container.size(200, 200).fill().align(align);
        container.setFillParent(true);
        return container;
    }
}
