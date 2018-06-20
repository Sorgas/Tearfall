package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.ui_components.TileStatusBar;
import stonering.game.core.view.ui_components.ToolStatus;
import stonering.game.core.view.ui_components.menus.MenuLevels;
import stonering.game.core.view.ui_components.menus.Toolbar;
import stonering.global.utils.Position;
import stonering.utils.global.StaticSkin;

/**
 * Stores and renders all ui components on screen.
 * Created by Alexander on 12.10.2017.
 */
public class UIDrawer {
    private GameMvc gameMvc;
    private Stage stage;
    private MenuLevels menuLevels;
    private TileStatusBar tileStatusBar;
    private Toolbar toolbar;
    private ToolStatus toolStatus;
    private GameContainer container;
    private MaterialMap materialMap;

    public UIDrawer(GameMvc gameMvc) {
        this.gameMvc = gameMvc;
        materialMap = MaterialMap.getInstance();
        menuLevels = new MenuLevels(gameMvc);
    }

    public void init() {
        container = gameMvc.getModel();
        stage = new Stage(new ScreenViewport());
        tileStatusBar = new TileStatusBar();
        stage.addActor(new Container(tileStatusBar).bottom().left().pad(10));

        menuLevels.init();
        Container container = new Container(menuLevels).bottom().right().pad(10);
        container.setFillParent(true);
        stage.addActor(container);

        toolStatus = new ToolStatus();

        stage.setDebugAll(true);
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
        Material material = materialMap.getMaterial(container.getLocalMap().getMaterial(focus));

        tileStatusBar.setData(focus, material != null ? material.getName() : "", container.getLocalMap().getArea(focus));
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
