package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.Plant;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Genarates and places {@link Plant} on local map.
 *
 * @author Alexander_Kuzyakov on 29.04.2019.
 */
public class LocalPlantsGenerator extends LocalFloraGenerator {

    public LocalPlantsGenerator(LocalGenContainer container) {
        super(container);
    }

    @Override
    protected Set<PlantType> filterPlantsByType() {
        TagLoggersEnum.GENERATION.log("generating plants");
        return PlantMap.getInstance().getPlantTypes().values().stream().filter(PlantType::isPlant).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        try {
            Collections.shuffle(positions);
            PlantGenerator plantGenerator = new PlantGenerator();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                if(plantContainer.getPlantBlocks().containsKey(position)) continue;
                Plant plant = plantGenerator.generatePlant(specimen, 0);
                plant.setPosition(position);
                container.model.get(PlantContainer.class).place(plant);
            }
        } catch (DescriptionNotFoundException e) {
            System.out.println("material for plant " + specimen + " not found");
        }
    }
}
