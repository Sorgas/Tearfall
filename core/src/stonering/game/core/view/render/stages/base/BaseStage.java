package stonering.game.core.view.render.stages.base;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.view.render.stages.InitableStage;
import stonering.game.core.view.render.stages.MapEntitySelectStage;
import stonering.game.core.view.render.stages.UIDrawer;
import stonering.util.geometry.Position;

/**
 * Stage with local world sprites and toolbar.
 * Is always rendered by {@link stonering.game.core.view.GameView}.
 *
 * @author Alexander on 09.11.2018.
 */
public class BaseStage extends InitableStage {
    private GameMvc gameMvc;
    private LocalWorldDrawer worldDrawer;
    private UIDrawer uiDrawer;
    private SpriteBatch batch;
    private EntitySelector entitySelector;

    public BaseStage(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        worldDrawer = new LocalWorldDrawer();
        uiDrawer = new UIDrawer(gameMvc);
        entitySelector = gameMvc.getModel().getCamera();
    }

    @Override
    public void init() {
        worldDrawer.init();
        uiDrawer.init();
    }

    @Override
    public void draw() {
        super.draw();
        worldDrawer.drawLocalWorld();
        uiDrawer.draw();
    }

    public void initBatch() {
        if (batch != null)
            batch.dispose();
        batch = new SpriteBatch();
        worldDrawer.setBatch(batch);
        worldDrawer.setScreenCenter(new Vector2(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f));
    }

    public void disposeBatch() {
        batch.dispose();
    }

    public LocalWorldDrawer getWorldDrawer() {
        return worldDrawer;
    }

    public UIDrawer getUiDrawer() {
        return uiDrawer;
    }

    /**
     * Handler for input. Firstly, toolbar is invoked, then entity selection.
     */
    @Override
    public boolean keyDown(int keyCode) {
        if (!uiDrawer.keyDown(keyCode)) {                 // try act with toolbar
            return trySelectMapEntity(keyCode);          // map click
        }
        return false;
    }

    /**
     * Called, if toolbar didn't handle event, shows selection list for map tile.
     *
     * @param keycode
     * @return
     */
    //TODO add filters like Shift+E Ctrl+E etc
    private boolean trySelectMapEntity(int keycode) {
        if (keycode == Input.Keys.E) {
            showMapEntityListStage(gameMvc.getModel().getCamera().getPosition());
            return true;
        }
        return false;
    }

    /**
     * Shows stage with list of entities in given position.
     * If there is only one, proceeds to entity stage immediately.
     *
     * @param position
     */
    //TODO add filters
    private void showMapEntityListStage(Position position) {
        gameMvc.getView().addStageToList(new MapEntitySelectStage(gameMvc, position, -1));
    }

    public void resize(int width, int height) {
        uiDrawer.resize(width, height);
    }
}
