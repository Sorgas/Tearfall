package stonering.stage.toolbar.menus;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.RenderAspect;
import stonering.entity.unit.aspects.OrientationAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.buildings.blueprint.BlueprintsMap;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.Position;
import stonering.widget.ToolbarSubMenuMenu;

import java.util.ArrayList;
import java.util.List;

import static stonering.enums.OrientationEnum.N;

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
                    EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
                    SelectionAspect selection = system.selector.getAspect(SelectionAspect.class);
                    BuildingType type = BuildingTypeMap.instance().getBuilding(blueprint.building);
                    selection.type = type;
                    setSpriteToSelector(system, type);
                    selection.validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
                    system.inputHandler.allowChangingZLevelOnSelection = false;
                    selection.selectHandler = box -> {
                        List<Position> positions = new ArrayList<>();
                        selection.boxIterator.accept(positions::add); // todo replace with orientation beans.
                        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, positions), true).show();
                    };
                }
            }, blueprint.menuPath);
        }
    }

    private void setSpriteToSelector(EntitySelectorSystem system, BuildingType type) {
        RenderAspect render = system.selector.getAspect(RenderAspect.class);
        if (type.construction) { // special sprites for constructions
            int x = ConstructionTileSelector.select(BlockTypeEnum.getType(type.passage));
            render.region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
        } else { // building sprites for buildings
            render.region = AtlasesEnum.buildings.getRegion(type.sprites[N.ordinal()], type.size);
            system.selector.getAspect(OrientationAspect.class).current = N;
        }
    }
}
