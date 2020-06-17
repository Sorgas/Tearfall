package stonering.game.model.system.liquid;

import static stonering.enums.blocks.BlockTypeEnum.*;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.ModelComponent;
import stonering.util.global.Updatable;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.util.*;

import com.sun.istack.Nullable;

/**
 * Container for all liquid tiles on map.
 * Liquids are stored as actors, modifying flooding array of local map.
 * Liquids fall down, flow horizontally and evaporate.
 * 
 * Container consists of maps of liquid tiles, and sources.
 * 
 * //TODO support other liquids
 * //TODO add intensity based on river/brook water amount
 * //TODO put water sources out of map borders
 *
 * @author Alexander on 22.08.2018.
 */
public class LiquidContainer implements ModelComponent, Initable, Updatable {
    private LocalMap localMap;
    private HashMap<Position, LiquidTile> liquidTiles;
    private HashMap<Position, LiquidTile> tempLiquidTiles;
    private HashMap<Position, LiquidSource> liquidSources;
    private int turnDelay = 10;
    private int turnCount = 0;

    public LiquidContainer() {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        tempLiquidTiles = new HashMap<>();
    }

    @Override
    public void init() {
        localMap = GameMvc.model().get(LocalMap.class);
        liquidTiles.keySet().forEach(position -> localMap.flooding.set(position, liquidTiles.get(position).amount));
    }

    /**
     * extracts collection from LocalGenContainer and creates entity of liquids.
     *
     * @param container LocalGenContainer.
     */
    public LiquidContainer loadWater(LocalGenContainer container) {
        container.waterTiles.forEach(position -> createLiquidTile(position, "water", 7));
        flushTempTiles();
        container.waterSources.forEach(position -> {
            liquidSources.put(position, new LiquidSource(position, MaterialMap.getId("water"), 1));
        });
        return this;
    }

    public void createLiquidSource(Position position, int liquid, int intensity) {
        liquidSources.put(position, new LiquidSource(position, liquid, intensity));
    }

    public LiquidTile createLiquidTile(Position position, String liquid, int amount) {
        return tempLiquidTiles.computeIfAbsent(position, position1 -> new LiquidTile(MaterialMap.getId(liquid), amount));
    }

    private void flushTempTiles() {
        liquidTiles.putAll(tempLiquidTiles);
        tempLiquidTiles.clear();
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
                .forEach(this::trySpreadTile); // move unstable liquids
        liquidSources.values().stream() // generate liquid in sources
                .filter(source -> getLiquidAmount(source.position) < 7)
                .forEach(source -> transferLiquid(null, source.position, 1));
        flushTempTiles();
    }
    
    private void trySpreadTile(Position position) {
        int currentWater = localMap.flooding.get(position);
        if(tryFallLower(position)) return;
        tryFlowToSide(position, currentWater);
    }

    private boolean tryFallLower(Position position) {
        if(position.z <= 0) return false;
        Position lowerPosition = Position.add(position, 0, 0, -1);
        BlockTypeEnum currentType = localMap.blockType.getEnumValue(position);
        BlockTypeEnum lowerType = localMap.blockType.getEnumValue(lowerPosition);
        if ((currentType == SPACE || currentType == DOWNSTAIRS)
                && lowerType != WALL        
                && localMap.flooding.get(lowerPosition) < 7) { // can fall lower
            transferLiquid(position, lowerPosition, 1);
            return true;
        }
        return false;
    }

    private boolean tryFlowToSide(Position position, int currentWater) {
        Position lowerPosition = Position.add(position,0,0, -1);
        return freeTilesAround(lowerPosition, 7).stream() // not full tiles
                .findAny()
                .map(foundPosition -> {
                    transferLiquid(position, foundPosition, 1);
                    return true;
                })
                .orElse(false);
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
                        && localMap.flooding.get(x, y, position.z) < maxWater) { // can take liquid
                    positions.add(new Position(x, y, position.z));
                }
            }
        }
        return positions;
    }
    
    private void transferLiquid(@Nullable Position from, @Nullable Position to, int amount) {
        if (to != null && localMap.inMap(to)) {
            LiquidTile acceptor = liquidTiles.get(to);
            if (acceptor == null) {
                acceptor = createLiquidTile(to, "water", 0);
            }
            acceptor.amount += amount;
            localMap.flooding.set(to, acceptor.amount);
        }
        if (from != null && localMap.inMap(from)) {
            LiquidTile source = liquidTiles.get(from);
            source.amount -= amount;
            localMap.flooding.set(from, source.amount);
        }
    }

    private int getLiquidAmount(Position position) {
        return Optional.ofNullable(liquidTiles.get(position))
                .map(tile -> tile.amount)
                .orElse(0);
    }
}
