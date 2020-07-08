package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import stonering.stage.util.UiStage;
import stonering.util.global.StaticSkin;
import stonering.screen.util.SimpleScreen;
import stonering.widget.TabbedPane;

/**
 * @author Alexander on 25.12.2019.
 */
public class TabbedPanelDemo extends Game {
    Container container;

    public static void main(String[] args) {
        new LwjglApplication(new TabbedPanelDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                Container<Tree> container = createContainer();
                stage.addActor(container);
                stage.addListener(new InputListener() {
                    @Override
                    public boolean keyDown(InputEvent event, int keycode) {
                        if (keycode == Input.Keys.ESCAPE) Gdx.app.exit();
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
        container = new Container();
        TabbedPane pane = new TabbedPane();
        Image image = new Image(StaticSkin.getColorDrawable(Color.RED));
        image.getDrawable().setMinWidth(200);
        image.getDrawable().setMinHeight(200);
        pane.add("red", image);
        image = new Image(StaticSkin.getColorDrawable(Color.YELLOW));
        image.getDrawable().setMinWidth(200);
        image.getDrawable().setMinHeight(200);
        pane.add("yellow", image);
        image = new Image(StaticSkin.getColorDrawable(Color.GREEN));
        image.getDrawable().setMinWidth(200);
        image.getDrawable().setMinHeight(200);
        pane.add("green", image);
        container.setFillParent(true);
        container.setActor(pane);
        return container;
    }
}
