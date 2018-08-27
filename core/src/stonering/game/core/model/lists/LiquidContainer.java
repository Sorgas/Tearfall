package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;

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
public class LiquidContainer {
    private LocalMap localMap;
    private HashMap<Position, LiquidTile> liquidTiles;
    private HashMap<Position, LiquidTile> tempLiquidTiles;
    private HashMap<Position, LiquidSource> liquidSources;
    private Random random;
    private int turnDelay = 40;
    private int turnCount = 0;
    private byte spaceCode;
    private byte wallCode;
    private byte floorCode;
    private byte rampCode;
    private byte stairsCode;
    private byte stairfloorCode;
    MaterialMap materialMap;

    public LiquidContainer(LocalGenContainer container) {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        tempLiquidTiles = new HashMap<>();
        random = new Random();
        materialMap = MaterialMap.getInstance();
        loadWater(container);
    }

    private void loadWater(LocalGenContainer container) {
        //TODO support other liquids
        container.getWaterTiles().forEach(position -> {
            createLiquidTile(position, "water", 7);
        });
        flushTempTiles();
        //TODO add intensity based on river/brook water amount
        container.getWaterSources().forEach(position -> {
            liquidSources.put(position, new LiquidSource(position, materialMap.getId("water"), 1));
        });
        cacheConstants();
    }

    private LiquidTile createLiquidTile(Position position, String liquid, int amount) {
        LiquidTile liquidTile = new LiquidTile(position, materialMap.getId(liquid), amount);
        tempLiquidTiles.put(position, liquidTile);
        return liquidTile;
    }

    private void flushTempTiles() {
        liquidTiles.putAll(tempLiquidTiles);
        tempLiquidTiles.clear();
    }

    private void cacheConstants() {
        spaceCode = BlockTypesEnum.SPACE.getCode();
        wallCode = BlockTypesEnum.WALL.getCode();
        floorCode = BlockTypesEnum.FLOOR.getCode();
        rampCode = BlockTypesEnum.RAMP.getCode();
        stairsCode = BlockTypesEnum.STAIRS.getCode();
        stairfloorCode = BlockTypesEnum.STAIRFLOOR.getCode();
    }

    public void turn() {
        turnCount++;
        if (turnCount == turnDelay) {
            turnCount = 0;
            for (Iterator<Position> iterator = liquidTiles.keySet().iterator(); iterator.hasNext(); ) {
                Position position = iterator.next();
                LiquidTile liquidTile = liquidTiles.get(position);
                if (liquidTile.amount > 0) { // can spread. tiles with
                    trySpreadTile(position);
                } else { // destroy zero tile
                    System.out.println("removing");
                    iterator.remove();
                }
            }
            flushTempTiles();
        }
    }

    private void trySpreadTile(Position position) {
        Position lowerPos = new Position(position.getX(), position.getY(), position.getZ() - 1);
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

    private ArrayList<Position> observeTilesAround(Position position, int currentAmountOfWater, boolean ignoreWaterAmount) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = position.getX() - 1; x < position.getX() + 2; x++) {
            for (int y = position.getY() - 1; y < position.getY() + 2; y++) {
                if (!(x == 0 && y == 0) && // not same point
                        localMap.inMap(x, y, position.getZ()) &&
                        localMap.isFlyPassable(x, y, position.getZ()) &&
                        (localMap.getFlooding(x, y, position.getZ()) < currentAmountOfWater || ignoreWaterAmount) &&
                        localMap.getFlooding(x,y,position.getZ()) < 7) { // can take liquid
                    positions.add(new Position(x, y, position.getZ()));
                }
            }
        }
        return positions;
    }


    private void transferLiquid(Position from, Position to, int amount) {
        LiquidTile source = liquidTiles.get(from);
        LiquidTile acceptor = liquidTiles.get(to);
        if (acceptor == null) {
            System.out.println("creating at: " + to);
            acceptor = createLiquidTile(to, "water", 0);
        }
        source.amount -= amount;
        localMap.setFlooding(from, source.amount);
        acceptor.amount += amount;
        localMap.setFlooding(to, acceptor.amount);
        System.out.println("transfer: " + from + " " + to);
    }

    public void initLiquidsToMap() {
        int counter = 0;
        liquidTiles.keySet().forEach(position -> {
            localMap.setFlooding(position.getX(), position.getY(), position.getZ(), liquidTiles.get(position).amount);
        });
        System.out.println("liquid tiles loaded: " + counter);
    }

    /**
     * Class for single tile of liquid. Position is taken from keyset of hashMap.
     */
    private class LiquidTile {
        int liquid;
        int amount;

        public LiquidTile(Position position, int liquid, int amount) {
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

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }
}
