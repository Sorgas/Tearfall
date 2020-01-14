package stonering.generators.localgen.generators.flora;

import stonering.entity.plant.SubstratePlant;
import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.system.SubstrateContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypesEnum.FLOOR;
import static stonering.enums.blocks.BlockTypesEnum.RAMP;

public class LocalSubstrateGenerator extends LocalFloraGenerator {
    private SubstrateContainer substrateContainer;
    private Set<Byte> substrateBlockTypes;

    public LocalSubstrateGenerator(LocalGenContainer container) {
        super(container);
        substrateBlockTypes = new HashSet<>(Arrays.asList(FLOOR.CODE, RAMP.CODE));
    }

    @Override
    protected void extractContainer() {
        super.extractContainer();
        substrateContainer = container.model.get(SubstrateContainer.class);
    }

    @Override
    protected Set<PlantType> filterPlantsByType() {
        Logger.GENERATION.log("generating small plants");
        return PlantTypeMap.getInstance().getSubstrateTypes().values().stream().filter(PlantType::isSubstrate).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        int counter = 0;
        try {
            Collections.shuffle(positions);
            PlantGenerator plantGenerator = new PlantGenerator();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                if (substrateContainer.isSubstrateBlockExists(position)) continue;
                SubstratePlant plant = plantGenerator.generateSubstrate(specimen, 0);
                container.model.get(SubstrateContainer.class).place(plant, position);
                counter++;
            }
        } catch (DescriptionNotFoundException e) {
            System.out.println("material for plant " + specimen + " not found");
        } finally {
            Logger.GENERATION.logDebug(counter + " substrates placed.");
        }
    }

    /**
     * Collects all tiles that can have substrates (floors and ramps).
     */
    @Override
    protected Set<Position> gatherPositions() {
        Set<Position> positions = new HashSet<>();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (!substrateBlockTypes.contains(localMap.getBlockType(x, y, z))) continue;
                    if (substrateContainer.isSubstrateBlockExists(cachePosition.set(x, y, z))) continue;
                    positions.add(new Position(x, y, z));
                }
            }
        }
        return positions;
    }
}
