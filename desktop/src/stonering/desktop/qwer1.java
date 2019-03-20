package stonering.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import stonering.game.view.render.stages.base.UiStage;
import stonering.screen.SimpleScreen;
import stonering.util.global.StaticSkin;

/**
 * @author Alexander on 19.02.2019.
 */
public class qwer1 {

    public static void main(String[] args) {
        GameMock gameMock = new GameMock();
        new LwjglApplication(gameMock);
    }

    private static class GameMock extends Game {

        @Override
        public void create() {
            setScreen(new ScreenMock());
        }
    }

    private static class ScreenMock extends SimpleScreen {
        private StageMock stageMock;

        public ScreenMock() {
            stageMock = new StageMock();
            Gdx.input.setInputProcessor(stageMock);
        }

        @Override
        public void render(float delta) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
            stageMock.act(delta);
            stageMock.getViewport().apply();
            stageMock.draw();
        }

        @Override
        public void resize(int width, int height) {
            super.resize(width, height);
            stageMock.resize(width, height);
        }
    }

    private static class StageMock extends UiStage {
        private Table table;

        public StageMock() {
            super();
            createStage();
//            addActor(createTable());
        }


        private void createStage() {
            Container container = new Container();
            container.setFillParent(true);
            container.align(Align.bottomLeft);
            container.setActor(createTable());
            addActor(container);
            container.setDebug(true, true);
            container = new Container();
            container.setFillParent(true);
            container.align(Align.bottomRight);
            container.setActor(createTable2());
            addActor(container);
            container.setDebug(true, true);
        }

        private Table createTable() {
            table = new Table();
            table.add(new TextButton("qwer1", StaticSkin.getSkin())).left().top().expand(true, true).fill();
            table.add(new TextButton("qwer2", StaticSkin.getSkin())).right().top().expand(true, true).row();
            table.add(new TextButton("qwer3", StaticSkin.getSkin())).left().bottom().expand(true, true);
            table.add(new TextButton("qwer4", StaticSkin.getSkin())).bottom().right().expand(true, true);
            table.setDebug(true, true);
            table.align(Align.bottomLeft);
            return table;
        }
        private Table createTable2() {
            table = new Table();
            table.add(new TextButton("asdf1", StaticSkin.getSkin())).left().top().expand(true, true).fill();
            table.add(new TextButton("asdf2", StaticSkin.getSkin())).right().top().expand(true, true);
            table.setDebug(true, true);
            table.align(Align.bottomRight);
//            table.setFillParent(true);
            return table;
        }
    }
}
