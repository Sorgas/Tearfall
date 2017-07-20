package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import stonering.game.core.controller.GameController;
import stonering.game.core.model.GameContainer;
import stonering.utils.Position;

/**
 * Created by Alexander on 10.06.2017.
 */
public class GameView implements Screen {
    private GameContainer container;
    private GameController controller;
    private LocalWorldDrawer worldDrawer;
    private Position camera;
    private SpriteBatch batch;

    public GameView(GameContainer container, GameController controller) {
        this.container = container;
        this.controller = controller;
    }

    @Override
    public void show() {
        camera = controller.getCamera();
        worldDrawer = new LocalWorldDrawer();
        initDrawer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        container.performTick();
        worldDrawer.drawWorld(container, camera);
    }

    @Override
    public void resize(int width, int height) {
        initDrawer();
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
        batch.dispose();
    }

    private void initDrawer() {
        batch = new SpriteBatch();
        worldDrawer.setBatch(batch);
        worldDrawer.setScreenCenterX(Gdx.graphics.getWidth() / 2);
        worldDrawer.setScreenCenterY(Gdx.graphics.getHeight() / 2);
        worldDrawer.setViewAreaWidth(30);
        worldDrawer.setViewAreDepth(10);
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }


}
