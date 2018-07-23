package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.buildings.BuildingMap;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.ItemContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.requirements.ItemsOnPositionRequirementAspect;
import stonering.objects.local_actors.building.Building;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.items.ItemSelector;

import java.util.ArrayList;

public class ConstructionEffectAspect extends EffectAspect {
    private GameContainer container;
    private String building;
    private String material;

    public ConstructionEffectAspect(Action action, String building, String material) {
        super(action, 100);
        this.building = building;
        this.material = material;
        container = action.getGameContainer();
    }

    @Override
    protected void applyEffect() {
        spendMaterials();
        createConstruction();
    }

    private void createConstruction() {
        container.getLocalMap().setBlock(action.getTargetPosition(), (byte) resolveBlockTypeByConstructionName(), MaterialMap.getInstance().getId(material));
    }

    private void spendMaterials() {
        ItemContainer itemContainer = container.getItemContainer();
        Position target = action.getTargetPosition();
        ArrayList<Item> items = itemContainer.getItems(target);
        ArrayList<ItemSelector> itemSelectors = ((ItemsOnPositionRequirementAspect) action.getRequirementsAspect()).getItemSelectors();
        itemSelectors.forEach(itemSelector -> {
            itemSelector.selectItems(items).forEach(item -> {
                itemContainer.removeItem(item);
            });
        });
    }

    private int resolveBlockTypeByConstructionName() {
        switch(building) {
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
}
