package stonering.test_chamber.model;

import stonering.entity.plant.AbstractPlant;
import stonering.entity.plant.Plant;
import stonering.exceptions.DescriptionNotFoundException;
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
        get(PlantContainer.class).place(createPlant(), new Position(5, 5, 2));
    }

    private AbstractPlant createPlant() {
        try {
            PlantGenerator plantGenerator = new PlantGenerator();
            Plant plant = plantGenerator.generatePlant("puffball_mushroom", 0);
            plant.setPosition(new Position(CENTER, CENTER, 2));
            return plant;
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
