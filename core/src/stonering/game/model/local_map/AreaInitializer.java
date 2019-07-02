package stonering.game.model.local_map;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.model.util.UtilByteArray;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

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
 * //TODO add areas for flying unit.
 *
 * @author Alexander on 31.01.2019.
 */
public class AreaInitializer {
    private LocalMap localMap;
    private PassageMap passage;
    private Set<Set<Byte>> synonyms; // sets contain numbers of connected areas
    private Map<Byte, Byte> areaMapping; // synonym values to synonym min
    private Position cachePosition;

    public AreaInitializer(LocalMap localMap) {
        this.localMap = localMap;
        cachePosition = new Position();
    }

    /**
     * Creates {@link PassageMap} based on localMap.
     *
     * @return
     */
    public void formPassageMap(PassageMap passage) {
        this.passage = passage;
        synonyms = new HashSet<>();
        areaMapping = new HashMap<>();
        initAreaNumbers();
        processSynonyms();
        applyMapping();
    }

    /**
     * Assigns initial area numbers to cells, generates synonyms.
     */
    private void initAreaNumbers() {
        byte areaNum = 1;
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    if (passage.getPassage(x, y, z) == BlockTypesEnum.PASSABLE) { // not wall
                        Set<Byte> neighbours = getNeighbours(x, y, z);
                        if (neighbours.isEmpty()) {
                            // new area found
                            passage.getArea().setValue(x, y, z, areaNum++);
                        } else {
                            if (neighbours.size() > 1) addSynonyms(neighbours); // multiple areas near.
                            passage.getArea().setValue(x, y, z, neighbours.iterator().next());
                        }
                    }
                }
            }
        }
    }

    /**
     * Maps values from synonyms to lowest value from synonym
     *
     */
    private void processSynonyms() {
        for (Set<Byte> synonym : synonyms) {
            byte min = Collections.min(synonym);
            for (Byte aByte : synonym) {
                if (areaMapping.keySet().contains(aByte))
                    Logger.LOADING.logWarn("Passage areas: not merged synonym intersection " + aByte);
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
        Map<Byte, Integer> areaNumbers = passage.getAreaNumbers();
        UtilByteArray area = passage.getArea();
        for (int x = 0; x < localMap.xSize; x++) {
            for (int y = 0; y < localMap.ySize; y++) {
                for (int z = 0; z < localMap.zSize; z++) {
                    oldArea = area.getValue(x, y, z); // unmapped value
                    if (oldArea == 0) continue; // non passable tile
                    newArea = areaMapping.getOrDefault(oldArea, oldArea); // area number can be not mapped, if area is isolated.
                    area.setValue(x, y, z, newArea); // set mapped value
                    areaNumbers.put(newArea, areaNumbers.getOrDefault(newArea, 0) + 1); // increment counter
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
        Set<Set<Byte>> intersectingSynonyms = synonyms.stream().filter(bytes -> !Collections.disjoint(bytes, neighbours)).collect(Collectors.toSet());
        // no synonyms are merged, new synonym is fully contained in another
        if (intersectingSynonyms.size() == 1 && intersectingSynonyms.iterator().next().containsAll(neighbours)) return;
        // merge all found synonyms
        synonyms.removeAll(intersectingSynonyms);
        Set<Byte> mergedSynonym = new HashSet<>();
        intersectingSynonyms.forEach(mergedSynonym::addAll);
        mergedSynonym.addAll(neighbours);
        synonyms.add(mergedSynonym);
    }

    /**
     * Returns area numbers of areas accessible from given position.
     * Does not return unset areas (0).
     */
    private Set<Byte> getNeighbours(int cx, int cy, int cz) {
        Set<Byte> neighbours = new HashSet<>();
        if (passage.getPassage(cx, cy, cz) != BlockTypesEnum.PASSABLE) return neighbours;
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    if (!passage.hasPathBetween(x, y, z, cx, cy, cz)) continue;
                    byte currentArea = passage.getArea().getValue(x, y, z);
                    neighbours.add(currentArea);
                }
            }
        }
        neighbours.remove((byte) 0);
        return neighbours;
    }
}
