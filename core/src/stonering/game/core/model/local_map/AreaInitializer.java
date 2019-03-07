package stonering.game.core.model.local_map;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.core.model.local_map.LocalMap;
import stonering.game.core.model.local_map.PassageMap;
import stonering.util.global.Pair;
import stonering.util.global.TagLoggersEnum;

import java.util.*;

/**
 * Utility for initing area numbers on game startup.
 * Isolated tiles get different numbers.
 * Works only for walkers.
 * <p>
 * Works is two map scan cycles.
 * <p>
 * Algorithm:
 * 1.1. if cell has no inited neighbours, new area number assigned, neighbour number assigned otherwise.
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
    private List<Pair<Integer, Integer>> sinonyms;
    private Set<Set<Byte>> synonyms; // sets contain numbers of connected areas
    private Map<Byte, Byte> areaMapping;

    public AreaInitializer(LocalMap localMap) {
        this.localMap = localMap;
    }

    public PassageMap initAreas() {
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
        sinonyms = new ArrayList<>();
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    if (isWalkPassable(x, y, z)) { // not wall
                        Set<Byte> neighbours = getNeighbours(x, y, z);
                        byte toSet = areaNum;
                        if (neighbours.size() == 0) {
                            areaNum++; // new area found
                        } else if (neighbours.size() > 1) {
                            toSet = addSynonyms(neighbours); // lowest synonym
                        } else {
                            toSet = neighbours.iterator().next(); // already revealed area
                        }
                        passageMap.getArea().setValue(x, y, z, toSet);
                    }
                }
            }
        }
    }

    /**
     * Adds given set to synonyms. Combines synonyms, if already encountered.
     */
    private byte addSynonyms(Set<Byte> neighbours) {
        for (Set<Byte> synonym : synonyms) {
            if (!Collections.disjoint(synonym, neighbours)) {
                synonym.addAll(neighbours);
                return synonym.iterator().next();
            }
        }
        synonyms.add(neighbours);
        return neighbours.iterator().next();
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
                areaMapping.put(min, aByte);
            }
        }
    }

    private void applyMapping() {
        byte area;
        for (int x = 0; x < localMap.getxSize(); x++) {
            for (int y = 0; y < localMap.getySize(); y++) {
                for (int z = 0; z < localMap.getzSize(); z++) {
                    area = passageMap.getArea().getValue(x, y, z);
                    passageMap.getArea().setValue(x, y, z, areaMapping.getOrDefault(area, area));
                }
            }
        }
    }

    private Set<Byte> getNeighbours(int cx, int cy, int cz) {
        Set<Byte> neighbours = new HashSet<>();
        for (int x = cx - 1; x < cx + 2; x++) {
            for (int y = cy - 1; y < cy + 2; y++) {
                for (int z = cz - 1; z < cz + 2; z++) {
                    byte currentArea = passageMap.getArea().getValue(x, y, z);
                    if (currentArea != 0) neighbours.add(currentArea);
                }
            }
        }
        return neighbours;
    }

    private boolean isWalkPassable(int x, int y, int z) {
        return BlockTypesEnum.getType(localMap.getBlockType(x, y, z)).PASSING == 2;
    }
}
