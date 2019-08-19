package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import org.lwjgl.openal.AL;
import stonering.game.view.render.stages.UiStage;
import stonering.game.view.render.ui.menus.toolbar.ToolbarButton;
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
        private Container container;
        private Table table;

        public ScreenMock() {
            stage = new UiStage();
            container = createContainer();
            stage.addActor(container);
            Gdx.input.setInputProcessor(stage);

            Container container2 = new Container();
            VerticalGroup group = new VerticalGroup();
//            group.align(Align.left);
            group.left();
            group.columnLeft();

            TextButton button = new TextButton("1", StaticSkin.getSkin());
            button.setColor(Color.GREEN);
            Container buttonContainer = new Container(button);
            buttonContainer.size(80,50);
            buttonContainer.align(Align.left);
            group.addActor(buttonContainer);

            button = new TextButton("2", StaticSkin.getSkin());
            buttonContainer = new Container(button);
            buttonContainer.size(80,50);
            buttonContainer.padLeft(20);
            group.addActor(buttonContainer);

            container2.setActor(group);
            container2.setFillParent(true);
            container2.align(Align.topLeft);
            container2.setDebug(true, true);
            stage.addActor(container2);
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
            stage.resize(width, height);
        }

        private void handleInput() {
            if (Gdx.input.isKeyJustPressed(Input.Keys.A)) zoom(0.1f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.S)) zoom(-0.1f);
        }

        private void zoom(float delta) {
            table.setScale(table.getScaleX() + delta, table.getScaleY() * delta);
            System.out.println(table.getWidth());

//            ((OrthographicCamera) stage.getCamera()).zoom += delta;
//            float zoom = ((OrthographicCamera) stage.getCamera()).zoom;
            container.setOriginX(container.getWidth() / 2);
            container.setScaleX(container.getScaleX() - delta);
            container.setOriginY(container.getHeight() / 2);
            container.setScaleY(container.getScaleY() - delta);
            container.layout();
//            container.setWidth(container.getWidth() + delta * 100);

//            stage.getViewport().setScreenSize((int) (Gdx.graphics.getWidth() * zoom), (int) (Gdx.graphics.getHeight() * zoom));
//            stage.getViewport().setWorldSize(Gdx.graphics.getWidth() * zoom, Gdx.graphics.getHeight() * zoom);
//            stage.getCamera().viewportWidth = (Gdx.graphics.getWidth() * zoom);
//            stage.getCamera().viewportHeight = (Gdx.graphics.getHeight() * zoom);
//            container.size(640 * zoom, 480 * zoom);
//            container.setPosition(container.getX() + 10, container.getY());
//            container.setScale(container.getScaleX() + delta);
//            int centerX = container.getWidth() / 2;
//            container.setX();
//            container.validate();
        }

        private Container createContainer() {
            Container container = new Container();
            container.align(Align.bottomLeft);
            table = new Table();
            table.add(new ToolbarButton("qwer1")).left().top().expand(true, true).fill().size(200, 200);
            table.add(new TextButton("qwer2", StaticSkin.getSkin())).right().top().expand(true, true);
            container.setActor(table);
            container.setDebug(true, true);
            return container;
        }
    }
}
