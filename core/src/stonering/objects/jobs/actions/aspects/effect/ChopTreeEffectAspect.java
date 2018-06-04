package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.game.core.model.lists.PlantContainer;
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
        PlantBlock block = container.getLocalMap().getPlantBlock(pos);
        if (block != null) {
            Plant plant = block.getPlant();
            if (plant.getType().isTree()) {
                cutTree(plant);
            } else {
                cutPlant(plant);
            }
        }
    }

    private void leaveLogs() {

    }

    private void cutTree(Plant plant) {
        PlantContainer plantContainer = container.getPlantContainer();
        Plant[][][] blocks3 = plant.getTree().getBlocks();
        for (Plant[][] blocks2 : blocks3) {
            for (Plant[] blocks1 : blocks2) {
                for (Plant plant1 : blocks1) {
                    if (plant1 != null) {
                        plantContainer.removePlant(plant1);
                        leavePlantProduct(plant1);
                        System.out.println("removed");
                    }
                }
            }
        }
    }

    private void cutPlant(Plant plant) {
        container.getPlantContainer().removePlant(plant);
        leavePlantProduct(plant);
    }

    private void leavePlantProduct(Plant plant) {
        Item item = new PlantProductGenerator().generateCutProduct(plant);
        if (item != null)
            container.getItemContainer().addItem(item, plant.getPosition());
    }
}
