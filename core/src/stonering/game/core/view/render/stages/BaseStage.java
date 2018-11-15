package stonering.game.core.view.render.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import stonering.game.core.GameMvc;
import stonering.game.core.view.render.scene.LocalWorldDrawer;
import stonering.global.utils.Position;

/**
 * Stage with local world sprites and toolbar.
 * Is always rendered by {@link stonering.game.core.view.GameView}.
 *
 * @author Alexander on 09.11.2018.
 */
public class BaseStage extends InvokableStage {
    private GameMvc gameMvc;
    private LocalWorldDrawer worldDrawer;
    private UIDrawer uiDrawer;
    private SpriteBatch batch;

    public BaseStage(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        worldDrawer = new LocalWorldDrawer(gameMvc);
        uiDrawer = new UIDrawer(gameMvc);
    }

    public void init() {
        worldDrawer.init();
        uiDrawer.init();
    }

    @Override
    public void draw() {
        super.draw();
        worldDrawer.drawWorld();
        uiDrawer.draw();
    }

    public void initBatch() {
        if (batch != null)
            batch.dispose();
        batch = new SpriteBatch();
        worldDrawer.setBatch(batch);
        worldDrawer.setScreenCenterX(Gdx.graphics.getWidth() / 2);
        worldDrawer.setScreenCenterY(Gdx.graphics.getHeight() / 2);
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

    @Override
    public boolean invoke(int keycode) {
        if(!uiDrawer.invoke(keycode)) {
            return trySelectMapEntity(keycode);
        }
        return false;
    }

    /**
     * Called, if toolbar didn't handle event, shows selection list for map tile.
     *
     * @param keycode
     * @return
     */
    //TODO add filters (modes)
    private boolean trySelectMapEntity(int keycode) {
        if(keycode == Input.Keys.E) {
            showMapEntityListStage(gameMvc.getModel().getCamera().getPosition());
            return true;
        }
        return false;
    }


    /**
     *
     * @param position
     */
    //TODO add modes
    private void showMapEntityListStage(Position position) {
        gameMvc.getView().addStageToList(new MapEntitySelectStage(gameMvc, position, -1));
    }
}
