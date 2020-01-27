package stonering.game.model.system;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.materials.MaterialMap;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.Updatable;
import stonering.game.model.local_map.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.util.geometry.Position;
import stonering.util.global.Initable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

/**
 * Container for all liquid actors. These actors modify localMap array.
 * Values from this array are taken in account by other actors.
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
    MaterialMap materialMap;

    public LiquidContainer() {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        tempLiquidTiles = new HashMap<>();
        materialMap = MaterialMap.instance();
        random = new Random();
    }

    @Override
    public void init() {
        localMap = GameMvc.instance().model().get(LocalMap.class);
        liquidTiles.keySet().forEach(position -> {
            localMap.setFlooding(position.x, position.y, position.z, liquidTiles.get(position).amount);
        });
    }

    /**
     * extracts collection from LocalGenContainer and creates entity of liquids.
     *
     * @param container LocalGenContainer.
     */
    public LiquidContainer loadWater(LocalGenContainer container) {
        //TODO support other liquids
        container.waterTiles.forEach(position -> {
            createLiquidTile(position, "water", 7);
        });
        flushTempTiles();
        //TODO add intensity based on river/brook water amount
        container.waterSources.forEach(position -> {
            liquidSources.put(position, new LiquidSource(position, materialMap.getId("water"), 1));
        });
        cacheConstants();
        return this;
    }

    private LiquidTile createLiquidTile(Position position, String liquid, int amount) {
        LiquidTile liquidTile = new LiquidTile(materialMap.getId(liquid), amount);
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
            for (Iterator<Position> iterator = liquidTiles.keySet().iterator(); iterator.hasNext(); ) {
                Position position = iterator.next();
                LiquidTile liquidTile = liquidTiles.get(position);
                if (liquidTile.amount > 0) { // can spread. tiles with
                    trySpreadTile(position);
                } else { // destroy zero tile
                    iterator.remove();
                }
            }
            liquidSources.keySet().forEach(position -> {
                if (liquidTiles.get(position) != null && liquidTiles.get(position).amount < 7)
                    transferLiquid(null, position, 1);
            });
            flushTempTiles();
        }
    }

    private void trySpreadTile(Position position) {
        Position lowerPos = new Position(position.x, position.y, position.z - 1);
        int currentWater = localMap.getFlooding(position);
        if (localMap.inMap(lowerPos)) { // check to flow lower
            byte currentBlockType = localMap.getBlockType(position);
            byte lowerBlockType = localMap.getBlockType(lowerPos);
            if ((currentBlockType == spaceCode || currentBlockType == stairfloorCode || // liquid falls from space and stairfloor
                    currentBlockType == stairsCode && lowerBlockType == stairfloorCode) && // liquid falls downstairs
                    lowerBlockType != wallCode && // can't fall into wall, bug case
                    localMap.getFlooding(lowerPos) < 7) { //can't fall into full tile
                transferLiquid(position, lowerPos, 1);
                return;
            } else { // can't fall directly down
                ArrayList<Position> positions = observeTilesAround(lowerPos, currentWater, true);
                if (!positions.isEmpty()) { // flow lower
                    transferLiquid(position, positions.get(random.nextInt(positions.size())), 1);
                    return;
                }
            }
        }
        if (currentWater > 1) {
            ArrayList<Position> positions = observeTilesAround(position, currentWater, false);
            if (!positions.isEmpty()) {
                transferLiquid(position, positions.get(random.nextInt(positions.size())), 1);
            }
        }
    }

    private ArrayList<Position> observeTilesAround(Position position, int currentAmountOfWater, boolean onLowerLevel) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = position.x - 1; x < position.x + 2; x++) {
            for (int y = position.y - 1; y < position.y + 2; y++) {
                if (!(x == 0 && y == 0) && // not same point
                        localMap.inMap(x, y, position.z) &&
                        localMap.isFlyPassable(x, y, position.z) &&
                        (localMap.getFlooding(x, y, position.z) < currentAmountOfWater || onLowerLevel) &&
                        localMap.getFlooding(x, y, position.z) < 7) { // can take liquid
                    positions.add(new Position(x, y, position.z));
                }
            }
        }
        if (localMap.isBorder(position) && !liquidSources.keySet().contains(position) && !onLowerLevel) {
            positions.add(new Position(-1, -1, -1)); // out of the map
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
    private class LiquidTile {
        int liquid;
        int amount;

        public LiquidTile(int liquid, int amount) {
            this.liquid = liquid;
            this.amount = amount;
        }
    }

    private class LiquidSource {
        Position position;
        int liquid;
        int intensity;

        public LiquidSource(Position position, int liquid, int intensity) {
            this.position = position;
            this.liquid = liquid;
            this.intensity = intensity;
        }
    }
}
