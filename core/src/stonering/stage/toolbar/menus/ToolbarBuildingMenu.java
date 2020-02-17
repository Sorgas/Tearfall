package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.buildings.blueprint.BlueprintsMap;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.stage.building.BuildingMaterialListStage;
import stonering.util.geometry.Position;
import stonering.widget.ToolbarSubMenuMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * ButtonMenu for selecting building.
 * Translates all blueprints from {@link BlueprintsMap} to buttons.
 * Constructions are treated the same as buildings here.
 * On selection, handler defines how many buildings will be created and their places,
 * and then shows menu for selecting materials to build.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class ToolbarBuildingMenu extends ToolbarSubMenuMenu {

    public ToolbarBuildingMenu(Toolbar toolbar) {
        super(toolbar);
        for (Blueprint blueprint : BlueprintsMap.getInstance().blueprints.values()) {
            addItem(blueprint.title, null, new ChangeListener() { //TODO add blueprint.icon
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
                    //TODO add building sprite to selector
                    system.setPositionValidator(PlaceValidatorsEnum.getValidator(blueprint.placing));
                    SelectionAspect aspect = system.selector.getAspect(SelectionAspect.class);
                    List<Position> positions = new ArrayList<>();
                    aspect.selectHandler = box -> {
                        aspect.boxIterator.accept(position -> {
                            positions.add(position); // todo replace with orientation beans.
                        });
                        GameMvc.view().addStage(new BuildingMaterialListStage(positions, blueprint));
                    };
                }
            }, blueprint.menuPath);
        }
    }

    @Override
    protected void onHide() {
        super.onHide();
    }
}
