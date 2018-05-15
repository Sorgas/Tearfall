package stonering.objects.jobs.actions.aspects.effect;

import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;
import stonering.objects.local_actors.plants.Plant;

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
        if(plant.getType().isTree()) {
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

    }
}
