package stonering.game.model.local_map;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.lists.BuildingContainer;
import stonering.game.model.lists.PlantContainer;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.global.TagLoggersEnum;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility for initializing area numbers on game startup.
 * Isolated tiles get different numbers.
 * Works only for walkers.
 * <p>
 * Works in two map scan cycles.
 * <p>
 * Algorithm:
 * 1.1. if cell has no initialized neighbours, new area number assigned, neighbour number assigned otherwise.
 * 1.2. if cell has neighbours with different numbers, these numbers are added as synonym.
 * 2.1. areas in synonyms are rewritten to have one number.
 * <p>
 * //TODO support doors, gates, bridges, etc.
 * //TODO add areas for flying units.
 *
 * @author Alexander on 31.01.2019.
 */
public class AreaInitializer {
    private LocalMap localMap;
    private PassageMap passageMap;
    private Set<Set<Byte>> synonyms; // sets contain numbers of connected areas
    private Map<Byte, Byte> areaMapping; // synonym values to synonym min
    private Position cachePosition;

    public AreaInitializer(LocalMap localMap) {
        this.localMap = localMap;
        cachePosition = new Position();
    }

    /**
     * Creates {@link PassageMap} based on localMap.
     * @return
     */
    public PassageMap formPassageMap() {
        passageMap = new PassageMap(localMap);
        synonyms = new HashSet<>();
        areaMapping = new HashMap<>();
        initAreaNumbers();
        processSynonyms();
        applyMapping();
        return passageMap;
    }

    /**
     * Assigns initial area numbers to cells, generates synonyms.
     */
    private void initAreaNumbers() {
        byte areaNum = 1;
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (isWalkPassable(x, y, z)) { // not wall
                        Set<Byte> neighbours = getNeighbours(x, y, z);
                        byte toSet = areaNum;
                        if (neighbours.size() == 0) {
                            areaNum++; // new area found
                        } else {
                            if (neighbours.size() > 1) addSynonyms(neighbours); // multiple areas near.
                            toSet = neighbours.iterator().next();
                        }
                        passageMap.getArea().setValue(x, y, z, toSet);
                    }
                }
            }
        }
    }

    /**
     * Maps values from synonyms to lowest value from synonym
     */
    private void processSynonyms() {
        for (Set<Byte> synonym : synonyms) {
            byte min = Collections.min(synonym);
            for (Byte aByte : synonym) {
                if (areaMapping.keySet().contains(aByte))
                    TagLoggersEnum.LOADING.logWarn("Passage areas: not merged synonym intersection " + aByte);
                areaMapping.put(aByte, min);
            }
        }
    }

    /**
     * Sets area numbers from mapping to passage map, so only minimal values from synonyms are used.
     * Also initializes cell counter for areas.
     */
    private void applyMapping() {
        byte oldArea;
        byte newArea;
        Map<Byte, Integer> areaNumbers = passageMap.getAreaNumbers();
        UtilByteArray area = passageMap.getArea();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    oldArea = area.getValue(x, y, z);
                    if (oldArea == 0) continue;
                    newArea = areaMapping.getOrDefault(oldArea, oldArea);
                    area.setValue(x, y, z, newArea);
                    areaNumbers.put(newArea, areaNumbers.getOrDefault(newArea, 0) + 1);
                }
            }
        }
        areaNumbers.keySet().forEach(byteA -> System.out.println(byteA + " " + areaNumbers.get(byteA)));
    }

    /**
     * Adds given set to synonyms. Combines synonyms, if already encountered.
     * Returns first number from synonym.
     */
    private void addSynonyms(Set<Byte> neighbours) {
        // synonyms, intersecting with new one
        Set<Set<Byte>> intersectingSynonyms =
                synonyms.stream().filter(bytes -> !Collections.disjoint(bytes, neighbours)).collect(Collectors.toSet());
        if (intersectingSynonyms.size() == 1 && intersectingSynonyms.iterator().next().containsAll(neighbours)) return;
        synonyms.removeAll(intersectingSynonyms);
        Set<Byte> mergedSynonym = new HashSet<>();
        intersectingSynonyms.forEach(bytes -> mergedSynonym.addAll(bytes));
        mergedSynonym.addAll(neighbours);
        synonyms.add(mergedSynonym);
    }

    /**
     * Returns area numbers of areas accessible from given position.
     */
    private Set<Byte> getNeighbours(int cx, int cy, int cz) {
        Set<Byte> neighbours = new HashSet<>();
        if (!isWalkPassable(cx, cy, cz)) return neighbours;
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    if (!hasPathBetween(x, y, z, cx, cy, cz)) continue;
                    byte currentArea = passageMap.getArea().getValue(x, y, z);
                    neighbours.add(currentArea);
                }
            }
        }
        neighbours.remove((byte) 0);
        return neighbours;
    }

    private boolean isWalkPassable(int x, int y, int z) {
        boolean value = localMap.inMap(x, y, z) &&
                BlockTypesEnum.getType(localMap.getBlockType(x, y, z)).PASSING == 2 &&
                GameMvc.instance().getModel().get(PlantContainer.class).isBlockPassable(cachePosition.set(x,y,z)) &&
                GameMvc.instance().getModel().get(BuildingContainer.class).isBlockPassable(cachePosition);
        return value;
    }

    public boolean hasPathBetween(int x1, int y1, int z1, int x2, int y2, int z2) {
        boolean passable1 = isWalkPassable(x1, y1, z1);
        boolean passable2 = isWalkPassable(x2, y2, z2);
        boolean sameLevel = z1 == z2;
        boolean lowRamp = BlockTypesEnum.getType(z1 < z2 ? localMap.getBlockType(x1, y1, z1) : localMap.getBlockType(x2, y2, z2)) == BlockTypesEnum.RAMP; // can descend on ramps
        return (passable1 && passable2 && (sameLevel || lowRamp));
    }
}
