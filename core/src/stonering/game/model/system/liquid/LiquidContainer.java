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
 *
 * @author Alexander on 22.08.2018.
 */
public class LiquidContainer implements ModelComponent, Initable, Updatable {
    private LocalMap localMap;
    private final HashMap<Position, LiquidTile> liquidTiles;
    private final HashMap<Position, LiquidSource> liquidSources;
    private int turnDelay = 10;
    private int turnCount = 0;
    private Set<Position> cache;
    private final Int2dBounds cacheBounds;
    private final Position cachePosition;
    private final Random random;

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
                    if (tryFallLower(pos)) {
                        getTile(pos.x, pos.y, pos.z - 1).stable = false; // set lower tile unstable
                    } else if (tryFlowToSide(pos)) {
                        cacheBounds.set(pos.x - 1, pos.y - 1, pos.x + 1, pos.y + 1)
                                .iterate((x, y) -> {
                                    Optional.ofNullable(getTile(x, y, pos.z))
                                            .ifPresent(tile -> tile.stable = false);
                                }); // set near tiles unstable
                    } else {
                        liquidTiles.get(pos).stable = true; // set current tile stable
                    }
                });

        liquidSources.values().stream() // generate liquid in sources
                .map(source -> source.position)
                .filter(position -> getAmount(position) < 7)
                .collect(Collectors.toList())
                .forEach(position -> transferLiquid(null, position, 1));
    }

    private boolean tryFallLower(Position position) {
        if (position.z > 0) {
            Position lowerPosition = Position.add(position, 0, 0, -1);
            BlockTypeEnum currentType = localMap.blockType.getEnumValue(position);
            BlockTypeEnum lowerType = localMap.blockType.getEnumValue(lowerPosition);
            if ((currentType == SPACE || currentType == DOWNSTAIRS)
                    && lowerType != WALL
                    && getAmount(lowerPosition) < 7) { // can fall lower
                transferLiquid(position, lowerPosition, 1);
                return true;
            }
        }
        return false;
    }

    private boolean tryFlowToSide(Position position) {
        if (getTile(position).amount < 2) return false;
        List<Position> positions = freeTilesAround(position, getAmount(position));
        if (positions.isEmpty()) return false;
        Position foundPosition = positions.get(random.nextInt(positions.size()));
        transferLiquid(position, foundPosition, 1);
        return true;
    }

    /**
     * Collects 8 tiles around position on same z-level which are open and contain less than maxWater
     */
    private List<Position> freeTilesAround(Position position, int maxWater) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = position.x - 1; x < position.x + 2; x++) {
            for (int y = position.y - 1; y < position.y + 2; y++) {
                if (!(x == 0 && y == 0)
                        && localMap.inMap(x, y, position.z)
                        && localMap.isFlyPassable(x, y, position.z)
                        && getAmount(x, y, position.z) < maxWater) { // can take liquid
                    positions.add(new Position(x, y, position.z));
                }
            }
        }
        return positions;
    }

    private void transferLiquid(@Nullable Position from, @Nullable Position to, int amount) {
        if (to != null && localMap.inMap(to)) setAmount(to, getAmount(to) + amount);
        if (from != null && localMap.inMap(from)) setAmount(from, getAmount(to) - amount);
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
        liquidTiles.computeIfAbsent(position.clone(), pos -> new LiquidTile(MaterialMap.getId("water"), 0)).amount = amount;
    }

    public LiquidTile getTile(Position position) {
        return liquidTiles.get(position);
    }

    public LiquidTile getTile(int x, int y, int z) {
        return liquidTiles.get(cachePosition.set(x, y, z));
    }
}
