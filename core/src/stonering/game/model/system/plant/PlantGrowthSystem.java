package stonering.game.model.system.plant;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Plant;
import stonering.entity.plant.Tree;
import stonering.entity.plant.aspects.PlantGrowthAspect;
import stonering.enums.plants.PlantLifeStage;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntitySystem;
import stonering.generators.plants.PlantGenerator;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;

/**
 * Rolls time for plants, increasing their age and changing {@link PlantLifeStage}.
 * On life stage change, changes plant blocks.
 * Plants get their blocks aspects updated (new sprite, new products).
 * Trees are completely rebuilt.
 *
 * @author Alexander on 14.01.2020.
 */
public class PlantGrowthSystem extends EntitySystem<AbstractPlant> {
    public int weekSize; // week size in hours
    private PlantGenerator plantGenerator = new PlantGenerator();
    private TreeGenerator treeGenerator = new TreeGenerator();
    
    public PlantGrowthSystem() {
        targetAspects.add(PlantGrowthAspect.class);
        updateInterval = TimeUnitEnum.HOUR;
    }

    /**
     * Increases growth counter(every minute) and plant age(counted in weeks).
     * When age increases, plant body is recreated with new life stage.
     * Should be updated every minute.
     */
    @Override
    public void update(AbstractPlant plant) {
        PlantGrowthAspect aspect = plant.get(PlantGrowthAspect.class);
        if (aspect.counter++ < getWeekSize()) return; // week not ended
        aspect.counter = 0;
        aspect.age++;
        if (aspect.age < plant.type.lifeStages.get(aspect.currentStage).stageEnd) return; // current stage not ended
        aspect.currentStage++;
        if (aspect.currentStage >= plant.type.lifeStages.size()) {
            killPlant(plant, aspect); // last stage ended
        } else {
            applyNewStage(plant); // new stage started
        }
    }

    /**
     * Changes plant loot and tree structure.
     */
    private void applyNewStage(AbstractPlant entity) {
        PlantContainer plantContainer = GameMvc.instance().model().get(PlantContainer.class);
        if (entity instanceof Tree) {
            Tree tree = (Tree) entity;
            plantContainer.removePlantBlocks(tree, false);
            treeGenerator.applyTreeGrowth(tree);
            plantContainer.add(tree, tree.position);
        } else if (entity instanceof Plant) {
            Plant plant = (Plant) entity;
            Position oldPosition = plant.getPosition();
            plantContainer.removePlantBlocks(plant, false);
            plantGenerator.applyPlantGrowth(plant);
            plantContainer.add(plant, oldPosition);
        }
    }

    private void killPlant(AbstractPlant plant, PlantGrowthAspect aspect) {
        aspect.dead = true; // TODO
        GameMvc.instance().model().get(PlantContainer.class).remove(plant, true);
    }

    private int getWeekSize() {
        return weekSize == 0 ? (weekSize = 7 * GameMvc.instance().model().getCalendar().day.max) : weekSize;
    }
}
