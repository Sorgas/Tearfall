package stonering.game.model.entity_selector.tool;

import stonering.entity.RenderAspect;
import stonering.entity.building.BuildingOrder;
import stonering.entity.crafting.IngredientOrder;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.enums.designations.PlaceValidatorEnum;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelectorSystem;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.local_map.passage.NeighbourPositionStream;
import stonering.game.model.system.task.DesignationSystem;
import stonering.game.model.system.task.TaskContainer;
import stonering.stage.entity_menu.building.ItemSelectSection;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.*;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Tool for designating constructions on local map.
 * Multiple constructions can be designated at once. Walls can be designated in horizontal rows of tiles.
 * Other constructions can be designated in 1 z-level boxes.
 *
 * @author Alexander on 22.03.2020
 */
public class ConstructionSelectionTool extends SelectionTool {
    private final Set<BlockTypeEnum> validConstructionTypes = new HashSet<>();
    private Blueprint blueprint;
    private BlockTypeEnum type;

    public ConstructionSelectionTool() {
        Collections.addAll(validConstructionTypes, FLOOR, DOWNSTAIRS, RAMP, STAIRS);
    }

    public SelectionTool setFor(Blueprint blueprint) {
        this.blueprint = blueprint;
        type = BlockTypeEnum.getType(blueprint.building);
        validator = PlaceValidatorEnum.getValidator(blueprint.placing);
        return this;
    }

    @Override
    public void apply() {
        int x = selectTileForConstruction(BlockTypeEnum.getType(blueprint.building));
        selector().get(RenderAspect.class).region = AtlasesEnum.ui_tiles.getBlockTile(x, 0);
        EntitySelectorSystem system = GameMvc.model().get(EntitySelectorSystem.class);
        system.allowChangingZLevelOnSelection = false;
        system.allowTwoDimensionsOnSelection = type != WALL;
        GameMvc.view().toolbarStage.showBuildingTab(blueprint);
    }

    @Override
    public void handleSelection(Int3dBounds bounds) {
        if (bounds.maxZ != bounds.minZ) {
            Logger.BUILDING.logError("Multiple z-level selection box in construction designation.");
            return;
        }
        LocalMap map = GameMvc.model().get(LocalMap.class);
        DesignationSystem designationSystem = GameMvc.model().get(TaskContainer.class).designationSystem;
        boolean valid = (type == WALL && validateWallDesignation(bounds))
                || (validConstructionTypes.contains(type) && validateConstructionDesignation(bounds));
        if(!valid) return;
        for (int x = bounds.minX; x <= bounds.maxX; x++) {
            for (int y = bounds.maxY; y >= bounds.minY; y--) {
                BlockTypeEnum blockType = map.blockType.getEnumValue(x, y, bounds.maxZ);
                if (blockType == WALL) continue;
                designationSystem.submitBuildingDesignation(createOrder(new Position(x, y, bounds.maxZ)), 1);
            }
        }
    }

    /**
     * At least one tile of bounds, should have adjacent passable tile.
     */
    private boolean validateConstructionDesignation(Int3dBounds bounds) {
        LocalMap map = GameMvc.model().get(LocalMap.class);
        Int3dBounds extendedBounds = bounds.clone();
        extendedBounds.extend(1);
        for (int x = extendedBounds.minX; x <= extendedBounds.maxX; x++) {
            for (int y = extendedBounds.maxY; y >= extendedBounds.minY; y--) {
                if (extendedBounds.isCorner(x, y) && map.blockType.getEnumValue(x, y, extendedBounds.maxZ).PASSING == PassageEnum.PASSABLE)
                    return true;
            }
        }
        return Logger.BUILDING.logError("No passable tile found near selected area.", false);
    }

    /**
     * Each tile of designation should have adjacent passable tile not included in designation.
     */
    private boolean validateWallDesignation(Int3dBounds bounds) {
        for (int x = bounds.minX; x <= bounds.maxX; x++) {
            for (int y = bounds.maxY; y >= bounds.minY; y--) {
                NeighbourPositionStream neighbours = new NeighbourPositionStream(new Position(x, y, bounds.maxZ), true);
                boolean hasPassableNeighbours = neighbours
                        .filter(position -> !bounds.isIn(position)) // tiles outside designation
                        .filterByPassage(PassageEnum.PASSABLE)
                        .stream.count() > 0;
                if (!hasPassableNeighbours)
                    return Logger.BUILDING.logWarn("No passable tile to build wall from.", false);
            }
        }
        return true;
    }

    private BuildingOrder createOrder(Position position) {
        BuildingOrder order = new BuildingOrder(blueprint, position);
        blueprint.ingredients.forEach((part, ingredient) -> {
            System.out.println("creating ingredient for " + part);
            ItemSelectSection section = GameMvc.view().toolbarStage.buildingTab.sectionMap.get(part);
            ItemSelector itemSelector = section.getItemSelector();
            order.ingredientOrders.put(part, new IngredientOrder(ingredient, itemSelector));
        });
        return order;
    }

    private int selectTileForConstruction(BlockTypeEnum type) {
        if (type != SPACE && type != FARM) return type.CODE - 1;
        Logger.BUILDING.logError("Invalid construction");
        return 0;
    }
}
