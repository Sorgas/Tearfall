package stonering.game.model.local_map;

import stonering.game.GameMvc;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;
import stonering.util.pathfinding.a_star.AStar;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static stonering.enums.blocks.BlockTypesEnum.PassageEnum.PASSABLE;

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
    private Position cachePosition;


    public PassageUpdater(LocalMap map, PassageMap passage) {
        this.map = map;
        this.passage = passage;
        cachePosition = new Position();
        aStar = GameMvc.instance().getModel().get(AStar.class);
    }

    /**
     * Called when local map passage is updated. If cell becomes non-passable, it may split area into two.
     */
    public void update(int x, int y, int z) {
        int passing = passage.isTilePassable(cachePosition.set(x, y, z));
        passage.passage.set(x, y, z, passing);
        if (passing == PASSABLE.VALUE) { // areas should be merged
            Set<Byte> areas = observeAreasAround(x, y, z);
            if (areas.size() == 1) passage.area.set(x, y, z, areas.iterator().next());
            if (areas.size() > 1) mergeAreas(areas);
        } else { // areas may split
            Position position = new Position(x, y, z);
            splitAreas(new NeighboursStream(position)
                            .filterOutOfMap()
                            .filterUnreachable()
                            .mapByArea(), position);
        }
    }

    /**
     * Return area numbers around given position.
     * Inaccessible tiles are skipped.
     */
    private Set<Byte> observeAreasAround(int cx, int cy, int cz) {

        Set<Byte> neighbours = new HashSet<>();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    if (!map.inMap(x, y, z)) continue;
                    byte areaValue = passage.area.get(x, y, z);
                    if (areaValue == 0 || !passage.hasPathBetweenNeighbours(x, y, z, cx, cy, cz)) continue;
                    neighbours.add(areaValue);
                }
            }
        }
        return neighbours;
    }

    /**
     * Merges all given areas into one, keeping number of largest one.
     */
    private void mergeAreas(Set<Byte> areas) {
        Logger.PATH.logDebug("Merging areas " + areas);
        if (areas.isEmpty()) return;
        byte largestArea = areas.stream().max(Comparator.comparingInt(o -> passage.area.numbers.get(o).value)).get();
        areas.remove(largestArea);
        HashMap<Byte, Integer> areaSizes = new HashMap<>();
        areas.forEach(aByte -> areaSizes.put(aByte, passage.area.numbers.get(aByte).value));
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
    private void splitAreas(Map<Byte, List<Position>> posMap, Position center) {
        Logger.PATH.logDebug("Splitting areas around " + center + " in positions " + posMap);
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
            new NeighboursStream(center)
                    .filterOutOfMap()
                    .filterByArea(value)
                    .filterUnreachable()
                    .stream.forEach(openSet::add);
        }
        return counter;
    }

    private byte getUnusedAreaNumber() {
        for (byte i = 0; i < Byte.MAX_VALUE; i++)
            if (!passage.area.numbers.keySet().contains(i)) return i;
        return 0;
    }

    private class NeighboursStream {
        private Position center;
        private Stream<Position> stream;

        public NeighboursStream(Position center) {
            this.center = center;
            Set<Position> neighbours = new HashSet<>();
            for (int x = center.x - 1; x < center.x + 2; x++) {
                for (int y = center.y - 1; y < center.y + 2; y++) {
                    for (int z = center.z - 1; z < center.z + 2; z++) {
                        neighbours.add(new Position(center.x + x, center.y + y, center.z + z));
                    }
                }
            }
            stream = neighbours.stream();
        }

        private NeighboursStream filterOutOfMap() {
            stream = stream.filter(map::inMap);
            return this;
        }

        private NeighboursStream filterUnreachable() {
            stream = stream.filter(position -> passage.hasPathBetweenNeighbours(position, center));
            return this;
        }

        private NeighboursStream filterByArea(int value) {
            stream = stream.filter(position -> passage.area.get(position) == value);
            return this;
        }

        private Map<Byte, List<Position>> mapByArea() {
            return stream.collect(Collectors.toMap(
                    position -> passage.area.get(position),
                    Arrays::asList,
                    (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    }));
        }
    }
}
