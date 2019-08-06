package stonering.game.model.local_map;

import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static stonering.enums.blocks.BlockTypesEnum.*;

/**
 * Holds lighting of map tiles, visible and not visible tiles(undiscovered by player).
 * Inits map tiles, handles revealing on digging.
 *
 * @author Alexander on 06.08.2019.
 */
public class LightMap {
    private LocalMap localMap;
    public float generalLight; // light of a whole map (day/night)
    public final UtilByteArray generalLightMap; // spreading of a daylight (multiplied to general light)
    public final UtilByteArray localLight; // holds light level from small sources -1 is for (added to general light)

    public LightMap(LocalMap localMap) {
        this.localMap = localMap;
        localLight = new UtilByteArray(localMap.xSize,localMap.ySize,localMap.zSize);
        generalLightMap = new UtilByteArray(localMap.xSize,localMap.ySize,localMap.zSize);
    }

    public void initLight() {
        hideMap();
        floodFillUnreveal(new Position(0, 0, localMap.zSize - 1), -1);
    }

    /**
     * Reveals tiles, visible from the start, and in the visibility range.
     * //TODO high lighting in start tile increace range.
     */
    private void handleDigging(Position start) {
        floodFillUnreveal(start, );
    }

    private void hideMap() {
        int counter = 0;
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    localMap.light.localLight.setValue(x, y, z, -1);
                    counter ++;
                }
            }
        }
        Logger.GENERATION.logDebug(counter + " tiles hidden.");
    }

    /**
     *
     * @param start Position to start filling
     * @param maxRange maximum distance from start, -1 for no limit
     */
    private void floodFillUnreveal(Position start, int maxRange) {
        LinkedHashSet<Position> open = new LinkedHashSet<>();
        Set<Position> closed = new HashSet<>();
        open.add(start);
        while (!open.isEmpty()) {
            Position pos = open.iterator().next();
            open.remove(pos);
            closed.add(pos);
            if(localLight.getValue(pos) != -1) continue; // tile already unrevealed
            if(maxRange != -1 && maxRange < start.getDistanse(pos)) continue; // terminate filling, if max range reached
            localLight.setValue(pos, (byte) 0); // reveal
            if (localMap.getBlockType(pos) == WALL.CODE) continue; // walls always terminate filling
            Set<Position> observed = observe(pos);
            observed.removeAll(closed);
            open.addAll(observed);
        }
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
                if (localMap.inMap(x, y, center.z) && !center.equals(x, y, center.z))
                    set.add(new Position(x, y, center.z)); // same as center
            }
        }

        byte blockType = localMap.getBlockType(center);
        Position lower = new Position(center.x, center.y, center.z - 1);
        if ((blockType == SPACE.CODE || blockType == STAIRS.CODE || blockType == STAIRFLOOR.CODE) && localMap.inMap(lower))
            set.add(lower);

        Position upper = new Position(center.x, center.y, center.z + 1);
        if (localMap.inMap(upper) && localMap.getBlockType(upper) == SPACE.CODE) set.add(upper);

        return set;
    }
}
