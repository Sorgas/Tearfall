package stonering.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import stonering.entity.local.building.Building;
import stonering.entity.local.building.BuildingType;
import stonering.entity.local.building.aspects.WorkbenchAspect;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.core.view.render.ui.components.menus.workbench.WorkbenchMenu;

/**
 * @author Alexander on 30.10.2018.
 */
public class WorkbenchMenuScreen implements Screen {
    private Stage stage;
    private Building mockWorkbench;
    private WorkbenchMenu menu;

    public WorkbenchMenuScreen() {
//        menu = new WorkbenchMenu();
    }

    @Override
    public void show() {
        stage = new Stage();
        stage.setDebugAll(true);
        stage.addActor(createMenu());
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(Gdx.gl20.GL_COLOR_BUFFER_BIT | Gdx.gl20.GL_DEPTH_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }

    private WorkbenchMenu createMenu() {
//        WorkbenchMenu menu = new WorkbenchMenu();
        return menu;
    }

    private void createWorkbench() {
        mockWorkbench = new Building(null, BuildingTypeMap.getInstance().getBuilding("forge"));
        mockWorkbench.addAspect(new WorkbenchAspect(mockWorkbench));
    }
}
