package stonering.entity.jobs.actions.aspects.effect;

import stonering.entity.jobs.actions.aspects.target.PlantActionTarget;
import stonering.generators.items.PlantProductGenerator;
import stonering.util.geometry.Position;
import stonering.entity.jobs.actions.Action;
import stonering.entity.local.items.Item;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.PlantBlock;
import stonering.util.global.TagLoggersEnum;

import java.util.ArrayList;

/**
 * @author Alexander on 25.09.2018.
 */
public class HarvestPlantEffectAspect extends EffectAspect {

    public HarvestPlantEffectAspect(Action action, int workAmount) {
        super(action, workAmount);
    }

    @Override
    protected void applyEffect() {
        System.out.println("harvesting plant");
        if (action.getTargetAspect() instanceof PlantActionTarget) {
            AbstractPlant abstractPlant = ((PlantActionTarget) action.getTargetAspect()).getPlant();
            Position position = action.getTargetAspect().getTargetPosition();
            PlantBlock block = action.getGameContainer().getLocalMap().getPlantBlock(position);
            if (block != null && block.getPlant() == abstractPlant) {
                PlantProductGenerator generator = new PlantProductGenerator();
                ArrayList<Item> items = generator.generateHarvestProduct(block);
                items.forEach(item -> action.getGameContainer().getItemContainer().putItem(item, position));
            }
        }
    }

    private void logFinish() {
        TagLoggersEnum.TASKS.logDebug("harvesting plant finished at " + action.getTargetPosition().toString() + " by " + action.getTask().getPerformer().toString());
    }
}
