package stonering.game.model.local_map.passage;

import stonering.enums.blocks.PassageEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;
import stonering.util.logging.Logger;
import stonering.util.pathfinding.AStar;

import java.util.*;
import java.util.stream.Collectors;

import static stonering.enums.blocks.PassageEnum.PASSABLE;

/**
 * Updates area and passage values in local map when tiles are changed.
 * When tile changes passage status, in can connect or split areas around it.
 *
 * @author Alexander on 14.11.2019.
 */
public class PassageUpdater {
    private LocalMap map;
    private PassageMap passage;
    private AStar aStar;

    public PassageUpdater(LocalMap map, PassageMap passage) {
        this.map = map;
        this.passage = passage;
        aStar = GameMvc.model().get(AStar.class);
    }

    /**
     * Called when local map passage is updated. If cell becomes non-passable, it may split area into two.
     */
    public void update(int x, int y, int z) {
        Position center = new Position(x, y, z);
        PassageEnum passing = passage.calculateTilePassage(center);
        passage.passage.set(center, passing.VALUE);
        if (passing == PASSABLE) { // tile became passable, areas should be merged
            Set<Byte> areas = new NeighbourPositionStream(center)
                    .filterConnectedToCenter()
                    .filterNotInArea(0)
                    .stream.map(position -> passage.area.get(position))
                    .collect(Collectors.toSet());
            // take new area number, if new tile is not connected to any area
            byte areaNumber = areas.isEmpty() ? getUnusedAreaNumber() : areas.iterator().next();
            passage.area.set(x, y, z, areaNumber); // set area value to current tile
            if (areas.size() > 1) mergeAreas(areas);
        } else { // tile became impassable, areas may split
            splitAreas(center, new NeighbourPositionStream(center)
                    .filterByPassage(PASSABLE)
                    .stream.collect(Collectors.toMap(
                            passage.area::get,
                            position -> new ArrayList<>(Arrays.asList(position)),
                            (list1, list2) -> {
                                list1.addAll(list2);
                                return list1;
                            }))
            );
        }
    }

    /**
     * Merges all given areas into one, keeping number of largest one.
     */
    private void mergeAreas(Set<Byte> areas) {
//        Logger.PATH.logDebug("Merging areas " + areas);
        if (areas.isEmpty()) return;
        byte largestArea = areas.stream().max(Comparator.comparingInt(o -> passage.area.numbers.get(o).value)).get();
        areas.remove(largestArea);
        for (int x = 0; x < map.xSize; x++) {
            for (int y = 0; y < map.ySize; y++) {
                for (int z = 0; z < map.zSize; z++) {
                    if (areas.contains(passage.area.get(x, y, z))) {
                        passage.area.set(x, y, z, largestArea);
                    }
                }
            }
        }
    }

    /**
     * Refills areas if they were split. Areas that were different before update cannot be merged.
     * Gets sets of tiles of same area and splits them into subsets of connected tiles.
     * If there were more than 1 subset(area has been split), refills such areas with new number.
     *
     * @param posMap area number is mapped to set of positions of this area.
     */
    private void splitAreas(Position center, Map<Byte, List<Position>> posMap) {
//        Logger.PATH.logDebug("Splitting areas around " + center + " in positions " + posMap);
        for (Byte areaValue : posMap.keySet()) {
            List<Position> posList = posMap.get(areaValue);
            if (posList.size() < 2) continue;
            List<Set<Position>> isolatedPositions = new ArrayList<>(); // positions in inner sets are connected.
            while (!posList.isEmpty()) {
                Position firstPos = posList.remove(0);
                Set<Position> connectedPositions = new HashSet<>();
                connectedPositions.add(firstPos);
                for (Iterator<Position> iterator = posList.iterator(); iterator.hasNext(); ) {
                    Position pos = iterator.next();
                    if (!(pos.isNeighbour(firstPos) && passage.hasPathBetweenNeighbours(pos, firstPos))
                            && aStar.makeShortestPath(pos, firstPos) == null)
                        continue; // skip inaccessible tiles.
                    iterator.remove();
                    connectedPositions.add(pos);
                }
                isolatedPositions.add(connectedPositions);
            }
            if (isolatedPositions.size() < 2) continue; // all positions from old areas remain connected, do nothing.
            isolatedPositions.remove(0);
            int oldCount = passage.area.numbers.get(areaValue).value;
            for (Set<Position> positions : isolatedPositions) {
                oldCount -= fill(positions.iterator().next(), getUnusedAreaNumber()); // refill isolated area with new number
            }
            if (passage.area.numbers.get(areaValue).value != oldCount)
                Logger.PATH.logWarn("Areas sizes inconsistency after split.");
        }
    }

    /**
     * Fills all tiles available from given with new area value.
     */
    private int fill(Position start, byte value) {
        int counter = 0;
        Set<Position> openSet = new HashSet<>();
        for (openSet.add(start); !openSet.isEmpty(); counter++) {
            Position center = openSet.iterator().next();
            openSet.remove(center);
            passage.area.set(center.x, center.y, center.z, value);
            new NeighbourPositionStream(center)
                    .filterConnectedToCenter()
                    .filterNotInArea(value)
                    .stream.forEach(openSet::add);
        }
        return counter;
    }

    private byte getUnusedAreaNumber() {
        for (byte i = 0; i < Byte.MAX_VALUE; i++)
            if (!passage.area.numbers.keySet().contains(i)) return i;
        Logger.PATH.logError("Area numbers overflow");
        return 0;
    }
}
