package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import stonering.game.view.render.stages.UiStage;
import stonering.game.view.render.ui.menus.toolbar.ToolbarButton;
import stonering.game.view.render.util.Scalable;
import stonering.screen.SimpleScreen;
import stonering.util.global.StaticSkin;

/**
 * Demo with some UI elements.
 *
 * @author Alexander on 19.02.2019.
 */
public class UiDemo extends Game {

    public static void main(String[] args) {
        new LwjglApplication(new UiDemo());
    }

    @Override
    public void create() {
        Screen screen = new ScreenMock();
        setScreen(screen);
    }

    private static class ScreenMock extends SimpleScreen {
        private UiStage stage;
        private ScalableTable scalableTable;
        private float scale;

        public ScreenMock() {
            stage = new UiStage();
            Container container = new Container();
            container.setFillParent(true);
            container.align(Align.center);
            container.setActor(scalableTable = new ScalableTable());
            container.setDebug(true, true);
            stage.addActor(container);
            Gdx.input.setInputProcessor(stage);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
            stage.act(delta);
            stage.getViewport().apply();
            stage.draw();
            handleInput();
        }

        @Override
        public void resize(int width, int height) {
            super.resize(width, height);
            stage.resize(width, height);
        }

        private void handleInput() {
            if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) scalableTable.rescale(scale += 0.1f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) scalableTable.rescale(scale -= 0.1f);
        }
    }

    private static class ScalableTable extends Table implements Scalable {
        private int WIDTH = 100;


        public ScalableTable() {
            add(new ToolbarButton("qwer1")).left().top().expand(true, true).fill();
            add(new TextButton("qwer2", StaticSkin.getSkin())).right().top().expand(true, true).row();
            add(new TextButton("qwer3", StaticSkin.getSkin())).left().bottom().expand(true, true);
            add(new TextButton("qwer4", StaticSkin.getSkin())).bottom().right().expand(true, true);
            rescale(1);
            setDebug(true, true);
        }

        @Override
        public void rescale(float scale) {
            setWidth(Math.max(WIDTH * scale, getMinWidth()));
            System.out.println(getWidth());
            System.out.println(getMinWidth());
            System.out.println(getPrefWidth());
            System.out.println();
        }
    }
}
