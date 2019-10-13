package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.building.Blueprint;
import stonering.enums.buildings.BlueprintsMap;
import stonering.game.GameMvc;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.widget.ToolbarSubMenuMenu;

/**
 * ButtonMenu for selecting building.
 * Translates all blueprints from {@link BlueprintsMap} to buttons.
 * Constructions are treated the same as buildings here.
 *
 * @author Alexander Kuzyakov on 25.01.2018.
 */
public class ToolbarBuildingMenu extends ToolbarSubMenuMenu {

    public ToolbarBuildingMenu(Toolbar toolbar) {
        super(toolbar);
        for (Blueprint blueprint : BlueprintsMap.getInstance().blueprints.values()) {
            addItem(blueprint.title, new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    GameMvc.instance().getController().getDesignationsController().setSequence(new BuildingDesignationSequence(blueprint));
                }
            }, blueprint.menuPath);
        }
    }

    @Override
    protected void onHide() {
        super.onHide();
    }
}
