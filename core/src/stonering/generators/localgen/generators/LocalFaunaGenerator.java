package stonering.generators.localgen.generators;

import stonering.game.model.system.unit.UnitContainer;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.creatures.CreatureGenerator;
import stonering.generators.localgen.LocalGenContainer;
import stonering.entity.unit.Unit;
import stonering.util.geometry.Position;

import static stonering.enums.blocks.BlockTypeEnum.SPACE;

/**
 * Generates wild animals on LocalMap.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class LocalFaunaGenerator extends LocalGenerator {
    private CreatureGenerator creatureGenerator;
    private LocalMap localMap;

    public LocalFaunaGenerator(LocalGenContainer container) {
        super(container);
        creatureGenerator = new CreatureGenerator();
    }

    public void execute() {
        localMap = container.model.get(LocalMap.class);
        Unit unit = creatureGenerator.generateUnit(selectPosition(), "human");
        if (unit != null) {
            container.model.get(UnitContainer.class).add(unit);
        }
    }

    private Position selectPosition() {
        Position position = new Position(localMap.xSize / 2 - 15, localMap.ySize / 2 , 0);
        position.z = findSurfaceZ(position.x, position.y);
        return position;
    }

    private int findSurfaceZ(int x, int y) {
        for (int z = localMap.zSize - 1; z >= 0; z--) {
            if (localMap.blockType.get(x, y, z) != SPACE.CODE) {
                return z;
            }
        }
        return 0;
    }
}
