package stonering.entity.local.plants.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.plants.AbstractPlant;

/**
 * Switches plant life stages. Restructures tree if needed to represent growth.
 *
 * @author Alexander on 13.02.2019.
 */
public class PlantGrowthAspect extends Aspect {
    public static final String NAME = "tree_growth";
    private static int MONTH_SIZE = GameCalendar.MONTH_SIZE * GameCalendar.DAY_SIZE * GameCalendar.HOUR_SIZE;

    private int counter = 0;

    public PlantGrowthAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
    }

    /**
     * Increases plant age if month has ended.
     */
    @Override
    public void turn() {
        counter++;
        if(counter == MONTH_SIZE) {
            counter = 0;
            AbstractPlant abstractPlant = (AbstractPlant) aspectHolder;
            if(abstractPlant.getCurrentStageIndex() != abstractPlant.increaceAge()) applyNewStage();
        }
    }

    /**
     * Changes plant loot and tree structure.
     */
    private void applyNewStage() {
        //TODO
    }

    @Override
    public String getName() {
        return NAME;
    }
}
