package stonering.generators.localgen.generators.flora;

import stonering.entity.plant.Plant;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.game.model.system.plant.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.Collections;
import java.util.Optional;
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
        return PlantTypeMap.instance().plantTypes.values().stream().filter(type -> type.isPlant).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        Collections.shuffle(positions);
        PlantGenerator plantGenerator = new PlantGenerator();
        for (int i = 0; i < amount && !positions.isEmpty(); i++) {
            Position position = positions.remove(0);
            if (!plantContainer.isPlantBlockExists(position))
                Optional.ofNullable(plantGenerator.generatePlant(specimen, 0))
                        .ifPresent(plant -> container.model.get(PlantContainer.class).add(plant, position));
        }
    }
}
