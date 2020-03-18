package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.buildings.blueprint.BlueprintsMap;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.tool.DesignateBuildingSelectionTool;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarSubMenuMenu;

/**
 * ButtonMenu for selecting buildings and constructions to build.
 * Translates all blueprints from {@link BlueprintsMap} to buttons.
 * On button press, {@link EntitySelector} goes into designating mod, allowing to select a box.
 * When box is selected, {@link BuildingMaterialSelectMenu} is shown, where player can chose materials for building.
 * After confirmation in menu, designations for building are created.
 * <p>
 * TODO add various designation sprites for ramps.
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
                    SelectionTools.BUILDING.resetFor(blueprint);
                    GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(SelectionTools.BUILDING);
                }
            }, blueprint.menuPath);
        }
    }
}
