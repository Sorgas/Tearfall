package stonering.game.model.local_map;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.GameModel;
import stonering.game.model.lists.BuildingContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;
import stonering.util.pathfinding.a_star.AStar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Subcomponent of {@link LocalMap}, is created on local map init.
 * Manages isolated areas on localMap to prevent pathfinding between them.
 * Updates areas on local map change.
 * When tile becomes passable, some areas may merge.
 * When tile becomes impassable, some areas may split.
 * Building walls performed in two steps, building floor above first, then building wall itself.
 * (for proper area update)
 * Destroying walls performed in opposite direction.
 *
 * @author Alexander on 05.11.2018.
 */
public class PassageMap {
    private LocalMap localMap;
    private AStar aStar;
    private UtilByteArray area; // number of area
    private UtilByteArray passage; /** see {@link BlockTypesEnum} for passage values. */
    private Map<Byte, Integer> areaNumbers; // counts number of cells in areas
    private Position cachePosition;

    public PassageMap(LocalMap localMap) {
        this.localMap = localMap;
        aStar = new AStar(localMap);
        areaNumbers = new HashMap<>();
        area = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
        passage = new UtilByteArray(localMap.xSize, localMap.ySize, localMap.zSize);
        cachePosition = new Position();
    }

    /**
     * Inits passage numbers for tiles.
     * @return
     */
    public PassageMap initPassage() {
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    passage.setValue(x, y, z, isTilePassable(cachePosition.set(x, y, z)));
                }
            }
        }
        return this;
    }

    /**
     * Called when local map passage is updated. If cell becomes non-passable, it may split area into two.
     */
    public void updateCell(int x, int y, int z) {
        int passing = isTilePassable(cachePosition.set(x, y, z));
        passage.setValue(x, y, z, passing);
        if (passing == BlockTypesEnum.PASSABLE) { // areas should be merged
            Set<Byte> areas = observeAreasAround(x, y, z);
            if(areas.size() == 1) area.setValue(x,y,z, areas.iterator().next());
            if (areas.size() > 1) mergeAreas(areas);
        } else { // areas may split
            splitAreas(collectNeighbourPositions(x, y, z), new Position(x, y, z));
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
                    if (!localMap.inMap(x, y, z)) continue;
                    byte areaValue = area.getValue(x, y, z);
                    if (areaValue == 0 || !hasPathBetween(x, y, z, cx, cy, cz)) continue;
                    neighbours.add(areaValue);
                }
            }
        }
        return neighbours;
    }

    /**
     * Refills areas if they were split. Areas that were different before update cannot be merged.
     * Gets sets of tiles of same area and splits them into subsets of connected tiles.
     * If there were more than 1 subset(area has been split), refills such areas with new number.
     *
     * @param posMap area number is mapped to set of positions of this area.
     */
    private void splitAreas(Map<Byte, List<Position>> posMap, Position center) {
        TagLoggersEnum.PATH.logDebug("Splitting areas around " + center + " in positions " + posMap);
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
                    if (!(pos.isNeighbour(firstPos) && hasPathBetween(pos, firstPos))
                            && aStar.makeShortestPath(pos, firstPos, true) == null)
                        continue; // skip inaccessible tiles.
                    iterator.remove();
                    connectedPositions.add(pos);
                }
                isolatedPositions.add(connectedPositions);
            }
            if (isolatedPositions.size() < 2) continue; // all positions from old areas remain connected, do nothing.
            isolatedPositions.remove(0);
            int oldCount = areaNumbers.get(areaValue);
            for (Set<Position> positions : isolatedPositions) {
                oldCount -= fill(positions.iterator().next(), getUnusedAreaNumber()); // refill isolated area with new number
            }
            if (areaNumbers.get(areaValue) != oldCount)
                TagLoggersEnum.PATH.logWarn("Areas sizes inconsistency after split.");
        }
    }

    /**
     * Merges all given areas into one, keeping number of largest one.
     */
    private void mergeAreas(Set<Byte> areas) {
        TagLoggersEnum.PATH.logDebug("Merging areas " + areas);
        if (areas.isEmpty()) return;
        Optional<Byte> largestAreaOptional = areas.stream().max(Comparator.comparingInt(o -> areaNumbers.get(o)));
        byte largestArea = largestAreaOptional.get();
        areas.remove(largestArea);
        HashMap<Byte, Integer> areaSizes = new HashMap<>();
        areas.forEach(aByte -> areaSizes.put(aByte, areaNumbers.get(aByte)));
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (areas.contains(area.getValue(x, y, z))) {
                        updateValue(x, y, z, largestArea);
                    }
                }
            }
        }
    }

    /**
     * Gathers all passable near positions and splits them by areas. Unassigned, inaccessible, impassable positions are skipped.
     */
    private Map<Byte, List<Position>> collectNeighbourPositions(int cx, int cy, int cz) {
        Map<Byte, List<Position>> positionLists = new HashMap<>();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    if (!localMap.inMap(x, y, z)) continue;
                    byte areaValue = area.getValue(x, y, z);
                    if (areaValue == 0 || !hasPathBetween(x, y, z, cx, cy, cz)) continue;
                    List<Position> positions = positionLists.getOrDefault(areaValue, positionLists.put(areaValue, new LinkedList<>()));
                    positions.add(new Position(x, y, z));
                }
            }
        }
        return positionLists;
    }

    /**
     * Fills all tiles available from given with new area value.
     */
    public int fill(Position start, byte value) {
        int counter = 0;
        Set<Position> openSet = new HashSet<>();
        for (openSet.add(start); !openSet.isEmpty(); counter++) {
            Position position = openSet.iterator().next();
            openSet.remove(position);
            updateValue(position.x, position.y, position.z, value);
            openSet.addAll(getNeighbours(position, value));
        }
        return counter;
    }

    /**
     * Returns neighbour positions, accessible from given one.
     */
    private Set<Position> getNeighbours(Position center, byte blockedArea) {
        Set<Position> neighbours = new HashSet<>();
        for (int x = center.x - 1; x < center.x + 2; x++) {
            for (int y = center.y - 1; y < center.y + 2; y++) {
                for (int z = center.z - 1; z < center.z + 2; z++) {
                    if (x == center.x && y == center.y && z == center.z) continue; // same position
                    Position position = new Position(center.x + x, center.y + y, center.z + z);
                    if (!localMap.inMap(position)) continue;             // neighbour is out of map
                    if (area.getValue(position) == blockedArea) continue; // same area
                    if (hasPathBetween(center, position)) neighbours.add(position);
                }
            }
        }
        return neighbours;
    }

    /**
     * Updates area value and counter.
     */
    private void updateValue(int x, int y, int z, byte value) {
        byte old = area.getValue(x, y, z);
        area.setValue(x, y, z, value);
        areaNumbers.put(value, areaNumbers.get(value) + 1);
        if (areaNumbers.get(old) < 2) {
            areaNumbers.remove(old);
        } else {
            areaNumbers.put(old, areaNumbers.get(old) - 1);
        }
    }

    private byte getUnusedAreaNumber() {
        for (byte i = 0; i < Byte.MAX_VALUE; i++)
            if (!areaNumbers.keySet().contains(i)) return i;
        return 0;
    }

    /**
     * Checks that walking creature can move from one tile to another.
     * Tiles should be adjacent.
     */
    public boolean hasPathBetween(int x1, int y1, int z1, int x2, int y2, int z2) {
        if (!localMap.inMap(x1, y1, z1)) return false;
        if (!localMap.inMap(x2, y2, z2)) return false;
        if (passage.getValue(x1, y1, z1) != BlockTypesEnum.PASSABLE) return false; // cell not passable
        if (passage.getValue(x2, y2, z2) != BlockTypesEnum.PASSABLE) return false; // cell not passable
        if (z1 == z2) return true; // passable tiles on same level
        return BlockTypesEnum.getType(z1 < z2 ? localMap.getBlockType(x1, y1, z1) : localMap.getBlockType(x2, y2, z2)) == BlockTypesEnum.RAMP; // can descend on ramps
    }

    public boolean hasPathBetween(Position pos1, Position pos2) {
        return hasPathBetween(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    /**
     * Tile is passable, if its block type allows walking(like floor, ramp, etc.), plant is passable(not tree trunk), building is passable.
     */
    private int isTilePassable(Position position) {
        GameModel model = GameMvc.instance().getModel();
        PlantContainer plantContainer = model.get(PlantContainer.class);
        BuildingContainer buildingContainer = model.get(BuildingContainer.class);
        if(plantContainer.getPlantBlocks().containsKey(position) && !plantContainer.getPlantBlocks().get(position).isPassable()) return BlockTypesEnum.NOT_PASSABLE;
        if(buildingContainer.getBuildingBlocks().containsKey(position) && !buildingContainer.getBuildingBlocks().get(position).isPassable()) return BlockTypesEnum.NOT_PASSABLE;
        //TODO add water depth checking, etc.
        return BlockTypesEnum.getType(localMap.getBlockType(position)).PASSING;
    }

    public byte getPassage(int x, int y, int z) {
        return passage.getValue(x, y, z);
    }

    public Map<Byte, Integer> getAreaNumbers() {
        return areaNumbers;
    }

    public UtilByteArray getArea() {
        return area;
    }
}
