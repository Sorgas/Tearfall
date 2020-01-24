package stonering.generators.localgen.generators.flora;

import stonering.entity.plant.Plant;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

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
        Logger.GENERATION.log("generating plants");
        return PlantTypeMap.getInstance().plantTypes.values().stream().filter(type -> type.isPlant).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        try {
            Collections.shuffle(positions);
            PlantGenerator plantGenerator = new PlantGenerator();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                if(plantContainer.isPlantBlockExists(position)) continue;
                Plant plant = plantGenerator.generatePlant(specimen, 0);
                container.model.get(PlantContainer.class).place(plant, position);
            }
        } catch (DescriptionNotFoundException e) {
            Logger.GENERATION.logError("material for plant " + specimen + " not found");
        }
    }
}
