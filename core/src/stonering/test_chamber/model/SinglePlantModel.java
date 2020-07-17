package stonering.test_chamber.model;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Plant;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing single plant stages.
 *
 * @author Alexander on 15.02.2019.
 */
public class SinglePlantModel extends TestModel {
    private static final int CENTER = MAP_SIZE / 2;

    @Override
    public void init() {
        super.init();
        get(PlantContainer.class).add(createPlant(), new Position(5, 5, 2));
    }

    private AbstractPlant createPlant() {
        PlantGenerator plantGenerator = new PlantGenerator();
        Plant plant = plantGenerator.generatePlant("radish", 0);
        plant.setPosition(new Position(CENTER, CENTER, 2));
        return plant;
    }
}
