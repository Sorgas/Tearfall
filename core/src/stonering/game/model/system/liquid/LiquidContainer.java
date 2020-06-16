package stonering.game.model.system.liquid;

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

/**
 * Container for all liquid actors. These actors modify localMap array.
 * Values from this array are taken in account by other actors.
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
    private Random random;
    private int turnDelay = 10;
    private int turnCount = 0;
    private byte spaceCode;
    private byte wallCode;
    private byte floorCode;
    private byte rampCode;
    private byte stairsCode;
    private byte stairfloorCode;

    public LiquidContainer() {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        tempLiquidTiles = new HashMap<>();
        random = new Random();
    }

    @Override
    public void init() {
        localMap = GameMvc.model().get(LocalMap.class);
        liquidTiles.keySet().forEach(position -> localMap.setFlooding(position, liquidTiles.get(position).amount));
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
        cacheConstants();
        return this;
    }

    private LiquidTile createLiquidTile(Position position, String liquid, int amount) {
        LiquidTile liquidTile = new LiquidTile(MaterialMap.getId(liquid), amount);
        tempLiquidTiles.put(position, liquidTile);
        return liquidTile;
    }

    private void flushTempTiles() {
        liquidTiles.putAll(tempLiquidTiles);
        tempLiquidTiles.clear();
    }

    private void cacheConstants() {
        spaceCode = BlockTypeEnum.SPACE.CODE;
        wallCode = BlockTypeEnum.WALL.CODE;
        floorCode = BlockTypeEnum.FLOOR.CODE;
        rampCode = BlockTypeEnum.RAMP.CODE;
        stairsCode = BlockTypeEnum.STAIRS.CODE;
        stairfloorCode = BlockTypeEnum.DOWNSTAIRS.CODE;
    }

    /**
     * Once in turnDelay turns tries to move all liquids if possible
     */
    @Override
    public void update(TimeUnitEnum unit) {
        turnCount++;
        if (turnCount == turnDelay) {
            turnCount = 0;
            liquidTiles.entrySet().removeIf(entry -> entry.getValue().amount == 0); // remove empty liquid tiles
            liquidTiles.keySet().forEach(this::trySpreadTile); // move liquids
            liquidSources.values().stream() // generate liquid in sources
                    .filter(source -> getLiquidAmount(source.position) < 7)
                    .forEach(source -> transferLiquid(null, source.position, 1));
            flushTempTiles();
        }
    }

    private void trySpreadTile(Position position) {
        Position lowerPos = new Position(position.x, position.y, position.z - 1);
        int currentWater = localMap.getFlooding(position);
        if (localMap.inMap(lowerPos)) { // check to flow lower
            byte currentBlockType = localMap.blockType.get(position);
            byte lowerBlockType = localMap.blockType.get(lowerPos);
            if ((currentBlockType == spaceCode || currentBlockType == stairfloorCode || // liquid falls from space and stairfloor
                    currentBlockType == stairsCode && lowerBlockType == stairfloorCode) && // liquid falls downstairs
                    lowerBlockType != wallCode && // can't fall into wall, bug case
                    localMap.getFlooding(lowerPos) < 7) { //can't fall into full tile
                transferLiquid(position, lowerPos, 1);
                return;
            } else { // can't fall directly down
                ArrayList<Position> positions = observeTilesAround(lowerPos, currentWater);
                if (!positions.isEmpty()) { // flow lower
                    transferLiquid(position, positions.get(random.nextInt(positions.size())), 1);
                    return;
                }
            }
        }
        if (currentWater > 1) {
            aroundTiles(position).stream()
                    .filter(localMap::inMap)
                    .filter(localMap::isFlyPassable)
                    .filter(nearPosition -> localMap.getFlooding(nearPosition) < currentWater)
                    .findAny()
                    .ifPresent(foundPosition -> transferLiquid(position, foundPosition, 1));
        }
    }

    /**
     * Collects 8 tiles around given position.
     */
    private List<Position> aroundTiles(Position position) {
        return new ArrayList<>();
    }
    
    private ArrayList<Position> observeTilesAround(Position position, int currentAmountOfWater) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = position.x - 1; x < position.x + 2; x++) {
            for (int y = position.y - 1; y < position.y + 2; y++) {
                // not same point
                if (!(x == 0 && y == 0) &&
                        localMap.inMap(x, y, position.z) &&
                        localMap.isFlyPassable(x, y, position.z) &&
                        localMap.getFlooding(x, y, position.z) < 7) { // can take liquid
                    positions.add(new Position(x, y, position.z));
                }
            }
        }
        return positions;
    }


    private void transferLiquid(Position from, Position to, int amount) {
        if (to != null && localMap.inMap(to)) {
            LiquidTile acceptor = liquidTiles.get(to);
            if (acceptor == null) {
                acceptor = createLiquidTile(to, "water", 0);
            }
            acceptor.amount += amount;
            localMap.setFlooding(to, acceptor.amount);
        }
        if (from != null && localMap.inMap(from)) {
            LiquidTile source = liquidTiles.get(from);
            source.amount -= amount;
            localMap.setFlooding(from, source.amount);
        }
    }

    /**
     * Class for single tile of liquid. Position is taken from keyset of hashMap.
     */
    private static class LiquidTile {
        int liquid;
        int amount;

        public LiquidTile(int liquid, int amount) {
            this.liquid = liquid;
            this.amount = amount;
        }
    }

    private static class LiquidSource {
        Position position;
        int liquid;
        int intensity;

        public LiquidSource(Position position, int liquid, int intensity) {
            this.position = position;
            this.liquid = liquid;
            this.intensity = intensity;
        }
    }

    private int getLiquidAmount(Position position) {
        return Optional.ofNullable(liquidTiles.get(position))
                .map(tile -> tile.amount)
                .orElse(0);
    }
}
