package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.ui_components.TileStatusBar;
import stonering.game.core.view.ui_components.ToolStatus;
import stonering.game.core.view.ui_components.Toolbar;
import stonering.global.utils.Position;

/**
 * Created by Alexander on 12.10.2017.
 */
public class UIDrawer {
    private Stage stage;
    private Table table;
    private TileStatusBar tileStatusBar;
    private Toolbar toolbar;
    private ToolStatus toolStatus;
    private GameContainer container;
    private MaterialMap materialMap;

    public UIDrawer() {
        materialMap = MaterialMap.getInstance();
        init();
    }

    private void init() {
        stage = new Stage(new ScreenViewport());
        stage.setDebugAll(true);

        table = new Table();
        table.setFillParent(true);

        tileStatusBar = new TileStatusBar();
        toolbar = new Toolbar();
        toolStatus = new ToolStatus();

        stage.addActor(tileStatusBar);
        stage.addActor(toolStatus);
        stage.addActor(toolbar);
    }

    public void draw() {
        updateStatusBar();
        stage.draw();
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }

    private void updateStatusBar() {
        Position focus = container.getCamera().getPosition();
        tileStatusBar.setData(focus, materialMap.getMaterial(container.getLocalMap().getMaterial(focus.getX(), focus.getY(), focus.getZ())).getName());
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
    }

    public Stage getStage() {
        return stage;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public ToolStatus getToolStatus() {
        return toolStatus;
    }
}
