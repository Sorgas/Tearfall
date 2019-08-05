package stonering.generators.localgen.generators;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Makes some tiles on local map hidden.
 *
 * @author Alexander on 05.08.2019.
 */
public class LocalRevealGenerator extends LocalAbstractGenerator {
    private LocalMap map;


    public LocalRevealGenerator(LocalGenContainer container) {
        super(container);
        map = container.model.get(LocalMap.class);
    }

    @Override
    public void execute() {
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                int value = 0;
                for (int z = map.zSize - 1; z <= 0; z++) {
                    if (map.getBlockType(x, y, z) != BlockTypesEnum.WALL.CODE) continue; // do not hide non wall tiles.
                    if (isHiddenWall(x, y, z)) map.generalLight.setValue(x, y, z, -1); // hide wall
                }
            }
        }
    }

    private void hideMap() {
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                for (int z = map.zSize - 1; z <= 0; z++) {
                    if (map.getBlockType(x, y, z) != BlockTypesEnum.WALL.CODE) break;
                    map.generalLight.setValue(x, y, z, -1);
                }
            }
        }
    }

    private void floodFill() {
        Set<Position> open = new HashSet<>();
        Set<Position> closed = new HashSet<>();
        open.add(new Position(0, 0, map.zSize - 1));
        while (!open.isEmpty()) {
            Position pos = open.iterator().next();
            map.generalLight.setValue(pos, (byte) 0); // reveal
            closed.add(pos);
            Set<Position> observed = observe(pos);
            observed.removeAll(closed);
            open.addAll(observed);
        }
    }

    private Set<Position> observe(Position center) {
        Set<Position> set = new HashSet<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (!map.inMap(x, y, z) // out of map
                            || map.getBlockType(x, y, z) == BlockTypesEnum.WALL.CODE // wall
                            || center.equals(x, y, z)) continue; // same as center
                    set.add(new Position(x, y, z));
                }
            }
        }
        return set;
    }

    /**
     * Wall is hidden, only if all tiles around is walls.
     */
    private boolean isHiddenWall(int cx, int cy, int cz) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (!map.inMap(cx + x, cy + y, cz)) continue;
                if (map.getBlockType(cx + x, cy + y, cz) != BlockTypesEnum.WALL.CODE) return false;
            }
        }
        return true;
    }
}
