package stonering.game.model.system.liquid;

import static stonering.enums.blocks.BlockTypeEnum.*;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.ModelComponent;
import stonering.util.geometry.Int2dBounds;
import stonering.util.global.Updatable;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.istack.Nullable;

/**
 * Container for all liquid tiles on map.
 * Liquids are stored as actors, modifying flooding array of local map.
 * Liquids fall down, flow horizontally and evaporate.
 * <p>
 * Container consists of maps of liquid tiles, and sources.
 * Liquid tiles can be stable and unstable. Only unstable ones can flow and fall.
 * When unstable tile moves, all tiles around it become unstable and will be updated on nrext turn.
 * Water tiles also become unstable when ground tiles change.
 * <p>
 * //TODO support other liquids
 * //TODO add intensity based on river/brook water amount
 * //TODO put water sources out of map borders
 * TODO add U-shaped behaviour
 *
 * @author Alexander on 22.08.2018.
 */
public class LiquidContainer implements ModelComponent, Initable, Updatable {
    private LocalMap localMap;
    private final Map<Position, LiquidTile> liquidTiles;
    private final HashMap<Position, LiquidSource> liquidSources;
    private int turnDelay = 10;
    private int turnCount = 0;
    private Set<Position> cache;
    private final Int2dBounds cacheBounds;
    private final Position cachePosition;
    private final Random random;
    private final int MAX_AMOUNT = 7;

    List<Position> neighbourDeltas = Arrays.asList(
            new Position(1, 0, 0),
            new Position(0, 1, 0),
            new Position(-1, 0, 0),
            new Position(0, -1, 0)
    );

    List<Position> allNeighbourDeltas = Arrays.asList(
            new Position(0, 1, 0),
            new Position(0, -1, 0),
            new Position(1, 0, 0),
            new Position(1, 1, 0),
            new Position(1, -1, 0),
            new Position(-1, 0, 0),
            new Position(-1, 1, 0),
            new Position(-1, -1, 0)
    );

    List<Position> lowerAndSameNeighbourDeltas = Arrays.asList(
            new Position(0, 1, 0),
            new Position(0, -1, 0),
            new Position(1, 0, 0),
            new Position(1, 1, 0),
            new Position(1, -1, 0),
            new Position(-1, 0, 0),
            new Position(-1, 1, 0),
            new Position(-1, -1, 0),
            new Position(0, 0, -1),
            new Position(0, 0, 1)
    );


    public LiquidContainer() {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        cache = new HashSet<>();
        cacheBounds = new Int2dBounds();
        cachePosition = new Position();
        random = new Random();
    }

    @Override
    public void init() {
        localMap = GameMvc.model().get(LocalMap.class);
    }

    /**
     * extracts collection from LocalGenContainer and creates entity of liquids.
     *
     * @param container LocalGenContainer.
     */
    public LiquidContainer loadWater(LocalGenContainer container) {
        container.waterTiles.forEach(position -> liquidTiles.put(position, new LiquidTile(MaterialMap.getId("water"), 7)));
        container.waterSources.forEach(position -> liquidSources.put(position, new LiquidSource(position, MaterialMap.getId("water"), 1)));
        return this;
    }

    public void createLiquidSource(Position position, int liquid, int intensity) {
        liquidSources.put(position, new LiquidSource(position, liquid, intensity));
    }

    @Override
    public void update(TimeUnitEnum unit) {
        turnCount++;
        if (turnCount == turnDelay) {
            turnCount = 0;
            updateLiquids();
        }
    }

    private void updateLiquids() {
        liquidTiles.entrySet().removeIf(entry -> entry.getValue().amount == 0); // remove empty liquid tiles

        liquidTiles.entrySet().stream()
                .filter(entry -> !entry.getValue().stable)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .forEach(pos -> {
                    if (!tryFallDown(pos) && !tryFallOver(pos) && !tryFlowToSide(pos)) getTile(pos).stable = true; // tile did not fall
                });

        liquidSources.values().stream() // generate liquid in sources
                .map(source -> source.position)
                .filter(position -> getAmount(position) < 7)
                .collect(Collectors.toList())
                .forEach(position -> createLiquidDelta(null, position, 1));
    }

    private boolean tryFallDown(Position position) {
        if (position.z <= 0) return false;
        Position lowerPosition = Position.add(position, 0, 0, -1);
        BlockTypeEnum currentType = localMap.blockType.getEnumValue(position);
        BlockTypeEnum lowerType = localMap.blockType.getEnumValue(lowerPosition);
        if ((currentType == SPACE || currentType == DOWNSTAIRS) && lowerType != WALL)
            if (getAmount(lowerPosition) < MAX_AMOUNT) { // can fall lower
                createLiquidDelta(position, lowerPosition, 1);
                return true;
            } else {
                Position pos = findPositionToTeleport(position);
                if(pos != null) {
                    createLiquidDelta(position, pos, 1);
                    return true;
                }
            }
        return false;
    }

    private boolean tryFallOver(Position position) {
        if (position.z <= 0) return false;
        List<Position> positions = neighbourDeltas.stream()
                .map(delta -> Position.add(position, delta))
                .filter(localMap::inMap)
                .filter(pos -> localMap.blockType.getEnumValue(pos).PASS_LIQUID_DOWN) // tile can pass liquid down
                .peek(pos -> pos.add(0, 0, -1)) // get lower positions
                .filter(pos -> localMap.blockType.getEnumValue(pos) != WALL) // tile can contain liquids
                .filter(pos -> getAmount(pos) < MAX_AMOUNT) // tile not full
                .collect(Collectors.toList());
        if (positions.isEmpty()) return false;
        Position foundPosition = positions.size() == 1
                ? positions.get(0)
                : positions.get(random.nextInt(positions.size()));
        createLiquidDelta(position, foundPosition, 1);
        return true;
    }

    private boolean tryFlowToSide(Position position) {
        int currentAmount = getAmount(position);
        if (currentAmount < 2) return false;
        List<Position> positions = allNeighbourDeltas.stream()
                .map(pos -> Position.add(position, pos)) // add deltas
                .filter(localMap::inMap) // in map
                .filter(pos -> localMap.blockType.getEnumValue(pos) != WALL) // non wall
                .filter(pos -> getAmount(pos) < currentAmount) // not full
                .collect(Collectors.toList());
        if (positions.isEmpty()) return false;
        Position foundPosition = positions.get(random.nextInt(positions.size()));
        createLiquidDelta(position, foundPosition, 1);
        allNeighbourDeltas.stream()
                .map(pos -> Position.add(position, pos)) // add deltas
                .map(this::getTile)
                .filter(Objects::nonNull)
                .forEach(tile -> tile.stable = false);
        return true;
    }

    private Position findPositionToTeleport(Position from) {
        System.out.println("teleporting from " + from);
        List<Position> open = new ArrayList<>();
        Set<Position> closed = new HashSet<>();
        open.add(from);
        while (!open.isEmpty()) {
            Position position = open.remove(0);
            List<Position> positions = lowerAndSameNeighbourDeltas.stream()
                    .map(pos -> Position.add(position, pos))
                    .filter(pos -> pos.z <= from.z)
                    .filter(localMap::inMap)
                    .filter(pos -> !closed.contains(pos))
                    .filter(pos -> localMap.blockType.getEnumValue(pos) != WALL)
                    .collect(Collectors.toList());
            for (Position pos : positions) {
                int amount = getAmount(pos);
                if (amount < MAX_AMOUNT) {
                    System.out.println("to " + pos);
                    return pos; // tp here
                }
                if (amount == MAX_AMOUNT) {
                    if (!closed.contains(pos)) open.add(pos); // new unchecked tile
                }
            }
            closed.add(position); // position checked
        }
        System.out.println("not found");
        return null;
    }

    private void createLiquidDelta(@Nullable Position from, @Nullable Position to, int amount) {
        if (to != null && localMap.inMap(to)) setAmount(to, getAmount(to) + amount);
        if (from != null && localMap.inMap(from)) setAmount(from, getAmount(from) - amount);
    }

    public int getAmount(Position position) {
        return Optional.ofNullable(getTile(position))
                .map(tile -> tile.amount)
                .orElse(0);
    }

    public int getAmount(int x, int y, int z) {
        return getAmount(cachePosition.set(x, y, z));
    }

    public void setAmount(Position position, int amount) {
        LiquidTile tile = liquidTiles.computeIfAbsent(position.clone(), pos -> new LiquidTile(MaterialMap.getId("water"), 0));
        tile.amount = amount;
        tile.stable = false;
    }

    public LiquidTile getTile(Position position) {
        return liquidTiles.get(position);
    }

    public LiquidTile getTile(int x, int y, int z) {
        return liquidTiles.get(cachePosition.set(x, y, z));
    }
}
