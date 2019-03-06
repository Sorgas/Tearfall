package stonering.game.core.view.render.ui.menus.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.local.building.Blueprint;
import stonering.entity.local.building.BuildingType;
import stonering.enums.buildings.BlueprintsMap;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.game.core.controller.controllers.designation.BuildingDesignationSequence;
import stonering.game.core.view.render.ui.menus.util.SubMenuMenu;
import stonering.util.global.TagLoggersEnum;

/**
 * ButtonMenu for selecting building.
 * Translates all blueprints from {@link BlueprintsMap} to buttons.
 * Constructions are treated the same as buildings here.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class BuildingMenu extends SubMenuMenu {

    public BuildingMenu() {
        super();
        hideable = true;
    }

    @Override
    public void init() {
        fillMenu();
        super.init();
    }

    private void fillMenu() {
        for (Blueprint blueprint : BlueprintsMap.getInstance().getBlueprints().values()) {
            BuildingType buildingType = BuildingTypeMap.getInstance().getBuilding(blueprint.getBuilding());
            if (buildingType == null) {
                TagLoggersEnum.BUILDING.logWarn("Building type " + blueprint.getBuilding() + " for blueprint " + blueprint.getName() + " not found.");
                continue;
            }
            addItem(blueprint.getTitle(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameMvc.getController().getDesignationsController().setActiveDesignation(new BuildingDesignationSequence(blueprint));
                }
            }, blueprint.getMenuPath());
        }
    }
}
