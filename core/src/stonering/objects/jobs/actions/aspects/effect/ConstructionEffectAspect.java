package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.building.Building;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;

public class ConstructionEffectAspect extends EffectAspect {
    private GameContainer container;
    private ArrayList<Item> materials;
    private String building;
    private String material;

    public ConstructionEffectAspect(Action action) {
        super(action, 100);
        container = action.getGameContainer();
    }

    @Override
    protected void applyEffect() {
        spendMaterials();
        container.getBuildingContainer().addBuilding(createBuilding());
    }

    private Building createBuilding() {
        Building building = new Building(action.getTargetPosition());
        building.setName("qwer");
        return building;
    }

    private void spendMaterials() {
        materials.forEach((item) -> container.getItemContainer().removeItem(item));
    }
}
