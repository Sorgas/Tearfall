package stonering.stage.toolbar;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.stage.UiStage;
import stonering.stage.building.BuildingMaterialTab;
import stonering.widget.TileStatusBar;

/**
 * Contains toolbar and status bar.
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class ToolbarStage extends UiStage {
    public Toolbar toolbar;
    private TileStatusBar tileStatusBar;
    private final Container<Actor> tabContainer;
    public BuildingMaterialTab buildingTab;

    public ToolbarStage() {
        super();
        addActor(toolbar = new Toolbar());
        addActor(tileStatusBar = new TileStatusBar());
        addActor(tabContainer = new Container<>());
        tabContainer.setFillParent(true);
        tabContainer.align(Align.bottomRight);
        setKeyboardFocus(toolbar);
        interceptInput = false;
    }

    public void draw() {
        if(tileStatusBar != null) tileStatusBar.update();
        super.draw();
    }

    public void showBuildingTab(Blueprint blueprint) {
        if(buildingTab == null) buildingTab = new BuildingMaterialTab();
        buildingTab.fillFor(blueprint);
        tabContainer.setActor(buildingTab);
    }

    public void hideTab() {
        tabContainer.setActor(null);
    }
}
