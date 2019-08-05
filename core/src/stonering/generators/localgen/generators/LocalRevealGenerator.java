package stonering.generators.localgen.generators;

import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.HashSet;
import java.util.Set;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * Marks some tiles on the map hidden.
 * Tile is hidden if it is not directly accessible from the surface.
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
        hideMap();
        floodFill();
    }

    private void hideMap() {
        int counter = 0;
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                for (int z = 0; z < map.zSize; z++) {
                    map.generalLight.setValue(x, y, z, -1);
                    counter ++;
                }
            }
        }
        Logger.GENERATION.logDebug(counter + " tiles hidden.");
    }

    private void floodFill() {
        Set<Position> open = new HashSet<>();
        Set<Position> closed = new HashSet<>();
        open.add(new Position(0, 0, map.zSize - 1));
        int counter = 0;
        while (!open.isEmpty()) {
            Position pos = open.iterator().next();
            open.remove(pos);
            map.generalLight.setValue(pos, (byte) 0); // reveal
            counter ++;
            closed.add(pos);
            if (map.getBlockType(pos) == WALL.CODE) continue; // walls always terminate filling
            Set<Position> observed = observe(pos);
            observed.removeAll(closed);
            open.addAll(observed);
        }
        Logger.GENERATION.logDebug(counter + " tiles shown.");
    }

    /**
     * Collects tiles around center. Center tile is visible.
     * All tiles are visible from non-wall tile.
     * Lower tile is visible, if current tile is not floor-like.
     * Only space tiles are revealed from down to up.
     */
    private Set<Position> observe(Position center) {
        Set<Position> set = new HashSet<>();
        for (int x = center.x - 1; x <= center.x + 1; x++) {
            for (int y = center.y - 1; y <= center.y + 1; y++) {
                if (map.inMap(x, y, center.z) && !center.equals(x, y, center.z))
                    set.add(new Position(x, y, center.z)); // same as center
            }
        }

        byte blockType = map.getBlockType(center);
        Position lower = new Position(center.x, center.y, center.z - 1);
        if ((blockType == SPACE.CODE || blockType == STAIRS.CODE || blockType == STAIRFLOOR.CODE) && map.inMap(lower))
            set.add(lower);

        Position upper = new Position(center.x, center.y, center.z + 1);
        if (map.inMap(upper) && map.getBlockType(upper) == SPACE.CODE) set.add(upper);

        return set;
    }
}
