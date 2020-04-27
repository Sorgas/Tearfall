package stonering.test_chamber.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import stonering.test_chamber.TestChamberGame;

public class TestChamberScreen implements Screen {
    private TestChamberGame testChamberGame;
    private TestModelSelectStage selectStage;

    public TestChamberScreen(TestChamberGame testChamberGame) {
        this.testChamberGame = testChamberGame;
    }

    @Override
    public void show() {
        selectStage = new TestModelSelectStage(testChamberGame);
        Gdx.input.setInputProcessor(selectStage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        selectStage.act(delta);
        selectStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        selectStage.resize(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
