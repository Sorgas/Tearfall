package stonering.game.core.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.scene.LocalWorldDrawer;
import stonering.game.core.view.render.ui.UIDrawer;
import stonering.game.core.view.render.ui.components.menus.util.Invokable;

/**
 * Screen of local map. {@link LocalWorldDrawer} for tile drawing, {@link UIDrawer for ui}
 *
 * @author Alexander Kuzyakov on 10.06.2017.
 */
public class GameView implements Screen, Invokable {
    private GameMvc gameMvc;
    private LocalWorldDrawer worldDrawer;
    private UIDrawer uiDrawer;
    private SpriteBatch batch;

    /**
     * Also creates all sub-components.
     *
     * @param gameMvc
     */
    public GameView(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        worldDrawer = new LocalWorldDrawer(gameMvc);
        uiDrawer = new UIDrawer(gameMvc);
    }

    /**
     * Do bindings of components to their controllers/models.
     */
    public void init() {
        worldDrawer.init();
        uiDrawer.init();
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

    private void initBatch() {
        if (batch != null)
            batch.dispose();
        batch = new SpriteBatch();
        worldDrawer.setBatch(batch);
        worldDrawer.setScreenCenterX(Gdx.graphics.getWidth() / 2);
        worldDrawer.setScreenCenterY(Gdx.graphics.getHeight() / 2);
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

    public UIDrawer getUiDrawer() {
        return uiDrawer;
    }

    public LocalWorldDrawer getWorldDrawer() {
        return worldDrawer;
    }

    @Override
    public boolean invoke(int keycode) {
        return uiDrawer.invoke(keycode); // first priority, returns false if no menus open
    }
}
