package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import stonering.game.core.controller.controllers.GameController;
import stonering.game.core.model.GameContainer;

/**
 * Created by Alexander on 10.06.2017.
 * <p>
 * Main game Screen
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
        initDrawer();
        uiDrawer.resize(width, height);
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
        batch = new SpriteBatch();
        worldDrawer.setContainer(container);
        uiDrawer.setContainer(container);
        worldDrawer.setBatch(batch);
        worldDrawer.setScreenCenterX(Gdx.graphics.getWidth() / 2);
        worldDrawer.setScreenCenterY(Gdx.graphics.getHeight() / 2);
        worldDrawer.setViewAreaWidth(50);
        worldDrawer.setViewAreDepth(15);
        controller.addInputProcessor(uiDrawer.getStage());
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

    public void init() {
        initDrawer();
    }
}
