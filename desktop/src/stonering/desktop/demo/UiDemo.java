package stonering.desktop.demo;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import stonering.enums.images.DrawableMap;
import stonering.stage.UiStage;
import stonering.util.global.StaticSkin;
import stonering.util.ui.SimpleScreen;

import java.awt.event.WindowEvent;

/**
 * Demo with some UI elements.
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {
    Table table;

    public static void main(String[] args) {
        new LwjglApplication(new UiDemo());
    }

    @Override
    public void create() {
        setScreen(new SimpleScreen() {
            private UiStage stage = new UiStage();

            {
                stage.interceptInput = false;
                stage.addActor(createContainer());
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
        });
    }

    private Container createContainer() {
        Container<Window> container = new Container();
        container.setFillParent(true);
        Window window = new Window("qwer", StaticSkin.getSkin());
        window.defaults().height(800);
        window.add(table = createTable()).width(300).align(Align.topLeft);
        window.add(createTable()).width(600);
        container.setActor(window);
        container.setDebug(true, true);
        return container;
    }

    private Table createTable() {
        Table table = new Table();
        table.defaults().expandX().fillX();
        table.add(new TextButton("qwer", StaticSkin.getSkin()));
        table.add(new TextButton("qwer", StaticSkin.getSkin())).row();
        table.add(new TextButton("qwer", StaticSkin.getSkin()));
        table.add(new TextButton("qwer", StaticSkin.getSkin()));
        table.align(Align.topLeft);
        return table;
    }
}
