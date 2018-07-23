package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.requirements.ItemsOnPositionRequirementAspect;
import stonering.objects.local_actors.building.Building;

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
        container.getBuildingContainer().addBuilding(createConstruction());
    }

    private Building createConstruction() {

//        container.getLocalMap().setBlocType(action.getTargetPosition(), BuildingMap.getInstance().getBuilding(building).getCategory());
        Building building = new Building(action.getTargetPosition());
        building.setName("qwer");
        return building;
    }

    private void spendMaterials() {
        ((ItemsOnPositionRequirementAspect) action.getRequirementsAspect()).getItemSelectors().forEach((item) -> container.getItemContainer().removeItem(item));
    }
}
