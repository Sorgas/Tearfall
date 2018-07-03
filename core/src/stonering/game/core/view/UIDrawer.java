package stonering.game.core.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisList;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.GameMvc;
import stonering.game.core.model.GameContainer;
import stonering.game.core.view.ui_components.TileStatusBar;
import stonering.game.core.view.ui_components.ToolStatus;
import stonering.game.core.view.ui_components.lists.NavigableList;
import stonering.game.core.view.ui_components.menus.MenuLevels;
import stonering.game.core.view.ui_components.menus.Toolbar;
import stonering.global.utils.Position;

/**
 * Sub model for ui items.
 * Stores and renders all ui components on screen.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class UIDrawer {
    private GameMvc gameMvc;
    private Stage stage;
    private MenuLevels menuLevels;
    private TileStatusBar tileStatusBar;
    private ToolStatus toolStatus;
    private GameContainer container;
    private MaterialMap materialMap;
    private Actor focusedActor;

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
        toolStatus = new ToolStatus();

        VerticalGroup rightTools = new VerticalGroup();
        rightTools.addActor(toolStatus);
        rightTools.addActor(menuLevels);
        Container container = new Container(rightTools).bottom().right().pad(10);
        container.setFillParent(true);
        stage.addActor(container);

        stage.setDebugAll(true);
    }

    public void draw() {
        updateStatusBar();
        stage.draw();
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
        return menuLevels.getToolbar();
    }

    public ToolStatus getToolStatus() {
        return toolStatus;
    }

    public void setContainer(GameContainer container) {
        this.container = container;
    }

    public MenuLevels getMenuLevels() {
        return menuLevels;
    }

    public boolean hasActiveList() {
        if(menuLevels.isMaterialSelectShown()) {
            return true;
        }
        return false;
    }


    public NavigableList getActiveList() {
        if(menuLevels.isMaterialSelectShown()) {
            return menuLevels.getMaterialSelectList();
        }
        return null;
    }
}
