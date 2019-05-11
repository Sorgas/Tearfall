package stonering.generators.localgen.generators.flora;

import stonering.entity.local.plants.SubstratePlant;
import stonering.entity.local.plants.Tree;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.plants.PlantMap;
import stonering.enums.plants.PlantType;
import stonering.exceptions.DescriptionNotFoundException;
import stonering.game.model.lists.PlantContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.plants.PlantGenerator;
import stonering.generators.plants.TreeGenerator;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypesEnum.FLOOR;
import static stonering.enums.blocks.BlockTypesEnum.RAMP;

public class LocalSubstrateGenerator extends LocalFloraGenerator {
    private Set<Byte> substrateBlockTypes;

    public LocalSubstrateGenerator(LocalGenContainer container) {
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
        int counter = 0;
        try {
            Collections.shuffle(positions);
            PlantGenerator plantGenerator = new PlantGenerator();
            for (int i = 0; i < amount; i++) {
                if (positions.isEmpty()) return;
                Position position = positions.remove(0);
                if (plantContainer.getSubstrateBlocks().containsKey(position)) continue;
                SubstratePlant plant = plantGenerator.generateSubstrate(specimen, 0);
                plant.setPosition(position);
                if(localMap.getBlockType(plant.getPosition()) == BlockTypesEnum.RAMP.CODE) System.out.println("qe");
                container.model.get(PlantContainer.class).place(plant);

                counter++;
            }
        } catch (DescriptionNotFoundException e) {
            System.out.println("material for plant " + specimen + " not found");
        } finally {
            TagLoggersEnum.GENERATION.logDebug(counter + " substrates placed.");
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
