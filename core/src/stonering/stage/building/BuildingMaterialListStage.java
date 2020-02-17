package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.stage.UiStage;
import stonering.util.geometry.Position;

import java.util.List;

/**
 * Shows list of building to build and allows selection of material items.
 *
 * @author Alexander on 17.02.2020
 */
public class BuildingMaterialListStage extends UiStage {
    private BuildingMaterialListMenu menu;

    public BuildingMaterialListStage(List<Position> positions, Blueprint blueprint) {
        Container<BuildingMaterialListMenu> container = new Container<>();
        container.setActor(new BuildingMaterialListMenu(blueprint, positions));
        container.setFillParent(true);
        addActor(container);
    }
}
