package stonering.generators.localgen.generators.flora;

import stonering.enums.plants.PlantTypeMap;
import stonering.enums.plants.PlantType;
import stonering.game.model.system.substrate.SubstrateContainer;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.blocks.BlockTypeEnum.FLOOR;
import static stonering.enums.blocks.BlockTypeEnum.RAMP;

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
        return PlantTypeMap.instance().substrateTypes.values().stream().filter(type -> type.isSubstrate).collect(Collectors.toSet());
    }

    @Override
    protected void placePlants(String specimen, float amount) {
        if (PlantTypeMap.getSubstrateType(specimen) == null) return;
        Collections.shuffle(positions);
        SubstrateContainer substrateContainer = container.model.get(SubstrateContainer.class);
        for (int i = 0; i < amount && !positions.isEmpty(); i++) {
            Position position = positions.remove(0);
            if (!substrateContainer.isSubstrateBlockExists(position))
                substrateContainer.place(generator.generateSubstrate(specimen, 0), position);
        }
    }

    /**
     * Collects all tiles that can have substrates (floors and ramps).
     */
    @Override
    protected Set<Position> gatherPositions() {
        Set<Position> positions = new HashSet<>();
        localMap.getBounds().iterate((x, y, z) -> {
            if (!substrateBlockTypes.contains(localMap.blockType.get(x, y, z))) return;
            if (!substrateContainer.isSubstrateBlockExists(cachePosition.set(x, y, z))) positions.add(new Position(x, y, z));
        });
        return positions;
    }
}
