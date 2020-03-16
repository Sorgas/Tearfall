package stonering.stage.toolbar;

import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.OrientationAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
import stonering.stage.toolbar.menus.ConstructionTileSelector;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.OrientationEnum.N;

/**
 * Class for setting tools to {@link stonering.game.model.entity_selector.EntitySelector}.
 * Called from various {@link stonering.widget.ToolbarButtonMenu}s.
 *
 * @author Alexander on 15.03.2020
 */
public class ToolSetter {

    public void setBlueprintForBuilding(Blueprint blueprint) {
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        SelectionAspect selection = system.selector.getAspect(SelectionAspect.class);
        BuildingType type = BuildingTypeMap.getBuilding(blueprint.building);
        selection.type = type;
        setSpriteToSelector(system, blueprint);
        selection.validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
        system.inputHandler.allowChangingZLevelOnSelection = false;
        selection.selectHandler = box -> {
            List<Position> positions = new ArrayList<>();
            selection.boxIterator.accept(positions::add); // todo replace with orientation beans.
            new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, positions), true).show();
        };
    }

    private void setSpriteToSelector(EntitySelectorSystem system, Blueprint blueprint) {
        RenderAspect render = system.selector.getAspect(RenderAspect.class);
        if (blueprint.construction) { // special sprites for constructions
            int x = ConstructionTileSelector.select(BlockTypeEnum.getType(blueprint.building));
            render.region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
        } else { // building sprites for buildings
            render.region = BuildingTypeMap.getBuilding(blueprint.building).getSprite(N);
            system.selector.getAspect(OrientationAspect.class).current = N;
        }
    }
}
