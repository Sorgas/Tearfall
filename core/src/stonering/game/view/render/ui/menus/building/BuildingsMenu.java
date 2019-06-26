package stonering.game.view.render.ui.menus.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.local.building.Blueprint;
import stonering.enums.buildings.BlueprintsMap;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.game.view.render.ui.menus.util.SubMenuMenu;

/**
 * ButtonMenu for selecting building.
 * Translates all blueprints from {@link BlueprintsMap} to buttons.
 * Constructions are treated the same as buildings here.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class BuildingsMenu extends SubMenuMenu {

    public BuildingsMenu() {
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
            addItem(blueprint.getTitle(), new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameMvc.getController().getDesignationsController().setActiveDesignation(new BuildingDesignationSequence(blueprint));
                }
            }, blueprint.getMenuPath());
        }
    }
}