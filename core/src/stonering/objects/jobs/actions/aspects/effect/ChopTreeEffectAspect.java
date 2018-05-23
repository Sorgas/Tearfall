package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.generators.items.PlantProductGenerator;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.jobs.actions.aspects.target.PlantTargetAspect;
import stonering.objects.local_actors.items.Item;
import stonering.objects.local_actors.plants.Plant;
import stonering.objects.local_actors.plants.PlantBlock;

/**
 * Effect for cutting plants and chopping trees.
 * After its work amount finishes action and leaves plant drops.
 */
public class ChopTreeEffectAspect extends EffectAspect {
    private GameContainer container;

    public ChopTreeEffectAspect(Action action) {
        super(action, 100);
        container = action.getGameContainer();
    }

    @Override
    protected void applyEffect() {
        Position pos = action.getTargetAspect().getTargetPosition();
        Plant plant = container.getLocalMap().getPlantBlock(pos).getPlant();
        if (plant.getType().isTree()) {
            cutTree();
        } else {
            cutPlant();
        }
    }

    private void leaveLogs() {

    }

    private void cutTree() {

    }

    private void cutPlant() {
        Position target = action.getTargetPosition();
        Plant targetPlant = ((PlantTargetAspect) action.getTargetAspect()).getPlant();
        if (container.getPlantContainer().getPlants().contains(targetPlant)) { //plant still persist
            container.getPlantContainer().removePlant(targetPlant);
            leavePlantProduct(targetPlant);
        }
    }

    private void leavePlantProduct(Plant plant) {
        Position target = action.getTargetPosition();
        Item item = new PlantProductGenerator().generateCutProduct(plant);
        if (item != null)
            container.getItemContainer().addItem(item, target);
    }
}
