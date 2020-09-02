package stonering.stage.toolbar;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.Align;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.stage.util.UiStage;
import stonering.stage.entity_menu.building.BuildingMaterialTab;
import stonering.stage.toolbar.rightbar.RightBar;
import stonering.widget.TileStatusBar;
import stonering.widget.util.KeyNotifierListener;

/**
 * Contains toolbar and status bar.
 * TODO add hotkeys for rightbar
 *
 * @author Alexander Kuzyakov on 12.10.2017.
 */
public class ToolbarStage extends UiStage {
    public Toolbar toolbar;
    private TileStatusBar tileStatusBar;
    public RightBar rightBar;
    private final Container<Actor> tabContainer;
    public BuildingMaterialTab buildingTab;

    public ToolbarStage() {
        super();
        addActor(toolbar = new Toolbar());
        addActor(tileStatusBar = new TileStatusBar());
        addActor(tabContainer = new Container<>());
        addActor(rightBar = new RightBar());
        tabContainer.setFillParent(true);
        tabContainer.align(Align.bottomRight);
        addListener(new KeyNotifierListener(rightBar));
        addListener(new KeyNotifierListener(toolbar));
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.RIGHT) {
                    System.out.println("rbm");
                    toolbar.removeLastMenu();
                    return true;
                }
                return false;
            }
        });
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
