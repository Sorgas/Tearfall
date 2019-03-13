package stonering.game.core.view.render.stages.base;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.game.core.GameMvc;
import stonering.game.core.model.EntitySelector;
import stonering.game.core.view.render.stages.MapEntitySelectStage;
import stonering.game.core.view.render.stages.PauseMenuStage;
import stonering.game.core.view.render.stages.UIDrawer;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

/**
 * Stage with local world sprites and toolbar.
 * Is always rendered by {@link stonering.game.core.view.GameView}.
 *
 * @author Alexander on 09.11.2018.
 */
public class BaseStage extends Stage implements Initable {
    private GameMvc gameMvc;
    private LocalWorldDrawer worldDrawer;
    private UIDrawer uiDrawer;

    public BaseStage() {
        gameMvc = GameMvc.getInstance();
        worldDrawer = new LocalWorldDrawer(gameMvc.getModel());
        uiDrawer = new UIDrawer();
    }

    @Override
    public void init() {
        uiDrawer.init();
    }

    @Override
    public void draw() {
        worldDrawer.draw();
        uiDrawer.draw();
    }

    public UIDrawer getUiDrawer() {
        return uiDrawer;
    }

    /**
     * Handler for input. Firstly, toolbar is invoked, then entity selection.
     */
    @Override
    public boolean keyDown(int keyCode) {
        if(uiDrawer.keyDown(keyCode))
            return true;
        if(tryShowPauseStage(keyCode))
            return true;
        return trySelectMapEntity(keyCode);
    }

    /**
     * Called, if toolbar didn't handle event, shows selection list for map tile.
     */
    //TODO add filters like Shift+E Ctrl+E etc
    private boolean trySelectMapEntity(int keycode) {
        if (keycode != Input.Keys.E) return false;
        showMapEntityListStage(gameMvc.getModel().get(EntitySelector.class).getPosition());
        return true;
    }

    /**
     * Shows stage with list of entities in given position.
     * If there is only one, proceeds to entity stage immediately.
     */
    //TODO add filters
    private void showMapEntityListStage(Position position) {
        gameMvc.getView().addStageToList(new MapEntitySelectStage(gameMvc, position, -1));
    }

    private boolean tryShowPauseStage(int keycode) {
        if (keycode != Input.Keys.Q) return false;
        gameMvc.getView().addStageToList(new PauseMenuStage());
        return true;
    }

    public void resize(int width, int height) {
        uiDrawer.resize(width, height);
        worldDrawer.resize(width, height);
    }
}
