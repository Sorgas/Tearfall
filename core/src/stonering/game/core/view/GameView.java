package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.model.GameContainer;

/**
 * Main game Screen
 *
 * Created by Alexander on 10.06.2017.
 */
public class GameView implements Screen {
    private GameContainer container;
    private GameController controller;
    private LocalWorldDrawer worldDrawer;
    private UIDrawer uiDrawer;
    private SpriteBatch batch;

    public GameView(GameContainer container) {
        this.container = container;
    }

    public void init() {
        initDrawer();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        worldDrawer.drawWorld();
        uiDrawer.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiDrawer.resize(width, height);
        initBatch();
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
        worldDrawer = new LocalWorldDrawer(container.getLocalMap());
        uiDrawer = new UIDrawer();
        worldDrawer.setContainer(container);
        uiDrawer.setContainer(container);
        worldDrawer.setScreenCenterX(Gdx.graphics.getWidth() / 2);
        worldDrawer.setScreenCenterY(Gdx.graphics.getHeight() / 2);
        worldDrawer.setViewAreaWidth(50);
        worldDrawer.setViewAreDepth(15);
        controller.addInputProcessor(uiDrawer.getStage());
    }

    private void initBatch() {
        if (batch != null)
            batch.dispose();
        batch = new SpriteBatch();
        worldDrawer.setBatch(batch);
        worldDrawer.setScreenCenterX(Gdx.graphics.getWidth() / 2);
        worldDrawer.setScreenCenterY(Gdx.graphics.getHeight() / 2);
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }

    public UIDrawer getUiDrawer() {
        return uiDrawer;
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }
}
