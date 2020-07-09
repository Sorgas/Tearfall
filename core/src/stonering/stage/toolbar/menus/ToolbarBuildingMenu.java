package stonering.stage.toolbar.menus;

import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.buildings.blueprint.BlueprintsMap;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.entity_selector.tool.SelectionTool;
import stonering.game.model.entity_selector.tool.SelectionTools;
import stonering.stage.entity_menu.building.BuildingMaterialSelectMenu;
import stonering.stage.toolbar.Toolbar;
import stonering.widget.ToolbarSubmenuMenu;

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
public class ToolbarBuildingMenu extends ToolbarSubmenuMenu {

    public ToolbarBuildingMenu(Toolbar toolbar) {
        super(toolbar);
        for (Blueprint blueprint : BlueprintsMap.getInstance().blueprints.values()) {
            addItem(blueprint.title, null, () -> { //TODO add blueprint.icon
                SelectionTool tool = blueprint.construction
                        ? SelectionTools.CONSTRUCTION.setFor(blueprint)
                        : SelectionTools.BUILDING.setFor(blueprint);
                GameMvc.model().get(EntitySelectorSystem.class).selector.get(SelectionAspect.class).set(tool);
            }, blueprint.menuPath);
        }
    }
}
