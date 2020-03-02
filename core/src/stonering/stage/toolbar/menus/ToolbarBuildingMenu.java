package stonering.stage.toolbar.menus;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.unit.aspects.RenderAspect;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.buildings.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.buildings.blueprint.BlueprintsMap;
import stonering.enums.designations.PlaceValidatorsEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.aspect.SelectionAspect;
import stonering.game.model.system.EntitySelectorSystem;
import stonering.stage.SingleWindowStage;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.stage.renderer.AtlasesEnum;
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
                    setSpriteToSelector(system, BuildingTypeMap.instance().getBuilding(blueprint.building));
                    system.setPositionValidator(PlaceValidatorsEnum.getValidator(blueprint.placing));
                    SelectionAspect aspect = system.selector.getAspect(SelectionAspect.class);
                    aspect.validator = PlaceValidatorsEnum.getValidator(blueprint.placing);
                    system.inputHandler.allowChangingZLevelOnSelection = false;
                    aspect.selectHandler = box -> {
                        List<Position> positions = new ArrayList<>();
                        aspect.boxIterator.accept(positions::add); // todo replace with orientation beans.
                        new SingleWindowStage<>(new BuildingMaterialSelectMenu(blueprint, positions), true).show();
                    };
                }
            }, blueprint.menuPath);
        }
    }

    private void setSpriteToSelector(EntitySelectorSystem system, BuildingType type) {
        TextureRegion region;
        if (type.construction) { // special sprites for constructions
            int x = ConstructionTileSelector.select(BlockTypeEnum.getType(type.passage));
            region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
        } else { // building sprite for buildings
            region = AtlasesEnum.buildings.getBlockTile(type.atlasXY);
        }
        system.selector.getAspect(RenderAspect.class).region = region;
    }
}
