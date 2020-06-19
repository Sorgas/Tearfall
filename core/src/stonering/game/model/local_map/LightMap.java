package stonering.game.model.local_map;

import com.badlogic.gdx.math.MathUtils;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static stonering.enums.blocks.BlockTypeEnum.*;

/**
 * Holds lighting of map tiles, visible and not visible tiles(undiscovered by player).
 * Inits map tiles, handles revealing on digging.
 * Tile lighting formula: generalLight *  + localLight
 * Light is measured from 0 to 7.
 *
 * @author Alexander on 06.08.2019.
 */
public class LightMap {
    private LocalMap localMap;
    public float generalLight; // light of a whole map (day/night) [0,1]
    public final UtilByteArray generalLightMap; // spreading of a daylight (multiplied to general light) [0,7]
    public final UtilByteArray localLight; // holds light level from small sources. -1 is for (added to general light) [0,7]

    private Position cachePosition;

    public LightMap(LocalMap localMap) {
        this.localMap = localMap;
        localLight = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
        generalLightMap = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
        cachePosition = new Position();
    }

    public void initLight() {
        hideMap();
        floodFillUnreveal(new Position(0, 0, localMap.zSize - 1), -1);
    }

    /**
     * Reveals tiles, visible from the start, and in the visibility range.
     * Should be called after changing block(s).
     * //TODO high lighting in start tile increases range.
     */
    public void handleDigging(Position start) {
//        floodFillUnreveal(start, getLightLevel(start) * 3);
        floodFillUnreveal(start, 20);
    }

    private void hideMap() {
        int counter = 0;
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    localMap.light.localLight.set(x, y, z, -1);
                    counter++;
                }
            }
        }
        Logger.GENERATION.logDebug(counter + " tiles hidden.");
    }

    /**
     * Reveals tiles connected to start.
     *
     * @param start    Position to start filling
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
            if (maxRange != -1 && maxRange < start.getDistance(pos))
                continue; // terminate filling, if max range reached
            localLight.set(pos, (byte) 0); // reveal
            if (localMap.blockType.get(pos) == WALL.CODE) continue; // walls always terminate filling
            Set<Position> observed = observe(pos);
            observed.removeAll(closed);
            open.addAll(observed);
        }
    }

    /**
     * Collects invisible tiles around center. All tiles with same z are visible from non-wall tile.
     * Lower tile is visible, if current tile is not floor-like. Only space tiles are revealed from down to up.
     * Stair and stairfloor tiles can be revealed from lower stairs.
     */
    private Set<Position> observe(Position center) {
        Set<Position> set = new HashSet<>();
        for (int x = center.x - 1; x <= center.x + 1; x++) {
            for (int y = center.y - 1; y <= center.y + 1; y++) {
                if (canReveal(cachePosition.set(x, y, center.z)))
                    set.add(cachePosition.clone()); // same as center
            }
        }
        if (canReveal(cachePosition.set(center.x, center.y, center.z - 1)) && checkZPairForReveal(cachePosition, center)) {
                set.add(cachePosition.clone());
        }
        if (canReveal(cachePosition.set(center.x, center.y, center.z + 1)) && checkZPairForReveal(center, cachePosition)) {
                set.add(cachePosition.clone());
        }
        return set;
    }

    /**
     * Check that tiles can be revealed from each other.
     */
    private boolean checkZPairForReveal(Position lower, Position upper) {
        BlockTypeEnum lowerType = BlockTypeEnum.getType(localMap.blockType.get(lower));
        BlockTypeEnum upperType = BlockTypeEnum.getType(localMap.blockType.get(upper));
        return upperType == SPACE || (lowerType == STAIRS && (upperType == STAIRS || upperType == DOWNSTAIRS));
    }

    /**
     * Checks that given tile is on map and hidden.
     */
    private boolean canReveal(Position pos) {
        return localMap.inMap(pos) && localLight.get(pos) == -1;
    }


    public byte getLightLevel(Position pos) {
        return (byte) MathUtils.clamp(generalLight * generalLightMap.get(pos) + localLight.get(pos), 0, 7);
    }
}
