package stonering.entity.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.local.plants.PlantBlock;
import stonering.entity.local.plants.Tree;

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
            AbstractPlant plant = block.getPlant();
            if (plant.getType().isTree()) {
                cutTree((Tree) plant);
            } else {
                cutPlant((Plant) plant);
            }
        }
    }

    private void leaveLogs() {

    }

    private void cutTree(Tree tree) {
        container.getPlantContainer().removeTree(tree);
    }

    private void cutPlant(Plant plant) {
        container.getPlantContainer().removePlant(plant);
    }
}
