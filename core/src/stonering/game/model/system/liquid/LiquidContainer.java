package stonering.game.model.system.liquid;

import stonering.enums.materials.MaterialMap;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.ModelComponent;
import stonering.util.lang.Updatable;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;

import java.util.*;
import java.util.stream.Stream;

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
public class LiquidContainer implements ModelComponent, Updatable {
    public final Map<Position, LiquidTile> liquidTiles;
    public final HashMap<Position, LiquidSource> liquidSources;
    public final Position cachePosition;
    private final LiquidMovingSystem movingSystem;
    private LocalMap map;

    public LiquidContainer() {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        cachePosition = new Position();
        movingSystem = new LiquidMovingSystem(this);
    }
    
    @Override
    public void update(TimeUnitEnum unit) {
        if (unit == TimeUnitEnum.TICK) {
            movingSystem.update();
        }
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
        tile.tpStable = false;
        map().updatePassage(position);
    }

    public LiquidTile getTile(Position position) {
        return liquidTiles.get(position);
    }

    public LiquidTile getTile(int x, int y, int z) {
        return liquidTiles.get(cachePosition.set(x, y, z));
    }

    public Stream<Position> liquidStream(int material) {
        return liquidTiles.entrySet().stream()
                .filter(entry -> entry.getValue().liquid == material)
                .map(Map.Entry::getKey);
    }

    private LocalMap map() {
        return map == null ? map = GameMvc.model().get(LocalMap.class) : map;
    }
}
