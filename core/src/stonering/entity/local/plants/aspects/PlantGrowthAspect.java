package stonering.entity.local.plants.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Tree;
import stonering.game.core.GameMvc;
import stonering.game.core.model.lists.PlantContainer;
import stonering.generators.plants.TreeGenerator;

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
        if (counter == MONTH_SIZE) {
            counter = 0;
            AbstractPlant abstractPlant = (AbstractPlant) aspectHolder;
            int current = abstractPlant.getCurrentStageIndex();
            int newStage = abstractPlant.increaceAge();
            System.out.println("-----------" + current + " " + newStage);
            if (newStage != current) applyNewStage();
        }
    }

    /**
     * Changes plant loot and tree structure.
     */
    private void applyNewStage() {
        PlantContainer plantContainer = GameMvc.getInstance().getModel().get(PlantContainer.class);
        if (aspectHolder instanceof Tree) {
            Tree tree = (Tree) aspectHolder;
            plantContainer.removePlantBlocks(tree);
            TreeGenerator treeGenerator = new TreeGenerator();
            treeGenerator.applyTreeGrowth(tree);
            plantContainer.placeTree(tree);
        }
    }

    @Override
    public String getName() {
        return NAME;
    }
}
