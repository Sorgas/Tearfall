package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.SubstratePlant;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypesEnum.FLOOR;
import static stonering.enums.blocks.BlockTypesEnum.RAMP;

public class LocalSubstrategenerator extends LocalFloraGenerator {
    private Set<Byte> substrateBlockTypes;

    public LocalSubstrategenerator(LocalGenContainer container) {
        super(container);
        substrateBlockTypes = new HashSet<>(Arrays.asList(FLOOR.CODE, RAMP.CODE));
    }

    @Override
    protected Set<PlantType> filterPlantsByType() {
        TagLoggersEnum.GENERATION.log("generating small plants");
        return PlantMap.getInstance().getSubstrateTypes().values().stream().filter(PlantType::isSubstrate).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        try {
            Collections.shuffle(positions);
            PlantGenerator plantGenerator = new PlantGenerator();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                SubstratePlant plant = plantGenerator.generateSubstrate(specimen, 0);
                plant.setPosition(position);
                container.model.get(PlantContainer.class).place(plant);
            }
        } catch (DescriptionNotFoundException e) {
            System.out.println("material for plant " + specimen + " not found");
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
                    if (plantContainer.getSubstrateBlocks().containsKey(cachePosition.set(x, y, z))) continue;
                    if (substrateBlockTypes.contains(localMap.getBlockType(x, y, z)))
                        positions.add(new Position(x, y, z));
                }
            }
        }
        return positions;
    }
}
