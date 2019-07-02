package stonering.test_chamber.model;

import stonering.entity.local.environment.GameCalendar;
import stonering.entity.local.plants.AbstractPlant;
import stonering.entity.local.plants.Plant;
import stonering.entity.world.World;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;

/**
 * Model for testing single plant stages.
 *
 * @author Alexander on 15.02.2019.
 */
public class SinglePlantModel extends TestModel {
    private static final int TREE_CENTER = MAP_SIZE / 2;

    @Override
    public void init() {
        super.init();
        get(GameCalendar.class).addListener("minute", get(World.class).getStarSystem());
        get(GameCalendar.class).addListener("minute", get(PlantContainer.class));
        get(PlantContainer.class).place(createPlant(), new Position(5, 5, 2));
    }

    private AbstractPlant createPlant() {
        try {
            PlantGenerator plantGenerator = new PlantGenerator();
            Plant plant = plantGenerator.generatePlant("puffball_mushroom", 0);
            plant.setPosition(new Position(TREE_CENTER, TREE_CENTER, 2));
            return plant;
        } catch (DescriptionNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
