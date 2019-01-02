package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.local.building.Building;
import stonering.entity.local.building.BuildingType;
import stonering.enums.buildings.BuildingTypeMap;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.jobs.actions.aspects.requirements.ItemsInPositionOrInventoryRequirementAspect;
import stonering.entity.local.items.Item;
import stonering.entity.local.items.selectors.ItemSelector;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;
import java.util.List;

public class ConstructionEffectAspect extends EffectAspect {
    private GameContainer container;
    private String buildingName;
    private String material;

    public ConstructionEffectAspect(Action action, String buildingName, String material) {
        super(action, 100);
        this.buildingName = buildingName;
        this.material = material;
        container = action.getGameContainer();
    }

    @Override
    protected void applyEffect() {
        logStart();
        spendMaterials();
        build();
    }

    private void build() {
        BuildingType buildingType = BuildingTypeMap.getInstance().getBuilding(buildingName);
        if(buildingType.getCategory().equals("constructions")) {
            container.getLocalMap().setBlock(action.getTargetPosition(), (byte) resolveBlockTypeByConstructionName(), MaterialMap.getInstance().getId(material));
        } else {
            Building building = container.getBuildingContainer().getBuildingGenerator().generateBuilding(buildingName, action.getTargetPosition());
            container.getBuildingContainer().addBuilding(building);
//            container.getLocalMap().setBuildingBlock(action.getTargetPosition(), building.getBlock());
        }
    }

    private void spendMaterials() {
        ItemContainer itemContainer = container.getItemContainer();
        Position target = action.getTargetPosition();
        ArrayList<Item> items = itemContainer.getItems(target);
        List<ItemSelector> itemSelectors = ((ItemsInPositionOrInventoryRequirementAspect) action.getRequirementsAspect()).getItemSelectors();
        itemSelectors.forEach(itemSelector -> {
            itemSelector.selectItems(items).forEach(item -> {
                itemContainer.removeItem(item);
            });
        });
    }

    private int resolveBlockTypeByConstructionName() {
        switch(buildingName) {
            case "wall" :
                return 1;
            case "floor" :
                return 2;
            case "ramp" :
                return 3;
            case "stairs" :
                return 4;
        }
        return 1;
    }

    private void logStart() {
        TagLoggersEnum.TASKS.logDebug("construction of " + buildingName + " started at " + action.getTargetPosition().toString() + " by " + action.getTask().getPerformer().toString());
    }
}
