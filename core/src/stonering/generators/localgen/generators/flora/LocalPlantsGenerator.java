package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.Plant;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Genarates and places {@link Plant} on local map.
 */
public class LocalPlantsGenerator extends LocalFloraGenerator {

    public LocalPlantsGenerator(LocalGenContainer container) {
        super(container);
    }

    @Override
    protected void filterPlantsByType() {
        commonPlantSet = new HashSet<>(commonPlantSet.stream().filter(type -> !type.isSubstrate() && !type.isTree()).collect(Collectors.toList()));
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        try {
            Collections.shuffle(positions);
            PlantGenerator plantGenerator = new PlantGenerator();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                Plant plant = plantGenerator.generatePlant(specimen, 0);
                plant.setPosition(position);
                container.plants.add(plant);
            }
        } catch (DescriptionNotFoundException e) {
            System.out.println("material for plant " + specimen + " not found");
        }
    }
}
