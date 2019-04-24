package stonering.generators.localgen.generators;

import stonering.game.model.local_map.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.local.unit.Unit;
import stonering.util.geometry.Position;

/**
 * Generates wild animals on LocalMap.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class LocalFaunaGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private CreatureGenerator creatureGenerator;

    public LocalFaunaGenerator(LocalGenContainer container) {
        this.container = container;
        config = container.config;
        creatureGenerator = new CreatureGenerator();
    }

    public void execute() {
        Unit unit = creatureGenerator.generateUnit("human");
        if (unit != null) {
            unit.setPosition(selectPosition());
            container.units.add(unit);
        }
    }

    private Position selectPosition() {
        Position position = new Position(container.localMap.xSize / 2 - 15, container.localMap.ySize / 2 , 0);
        position.z = findSurfaceZ(position.x, position.y);
        return position;
    }

    private int findSurfaceZ(int x, int y) {
        LocalMap localMap = container.localMap;
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.getBlockType(x, y, z) != 0) {
                return z;
            }
        }
        return 0;
    }
}
