package stonering.game.core.model.lists;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.LocalMap;
import stonering.generators.localgen.LocalGenContainer;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Container for all units of liquids.
 * Liquids are stored only here and update array in local map when moving.
 * Other actors look to local map only when finding paths with minimal flooding.
 * <p>
 * Other actors may create/consume/dispose liquids from this container(pipes, creatures with buckets).
 *
 * @author Alexander on 22.08.2018.
 */
public class LiquidContainer {
    private LocalMap localMap;
    private HashMap<Position, LiquidTile> liquidTiles;
    private HashMap<Position, LiquidSource> liquidSources;
    private Random random;
    private int turnDelay = 100;
    private int turnCount = 0;

    public LiquidContainer(LocalGenContainer container) {
        liquidTiles = new HashMap<>();
        liquidSources = new HashMap<>();
        random = new Random();
        loadWater(container);
    }

    /**
     * extracts collection from LocalGenContainer and creates objects of liquids.
     *
     * @param container LocalGenContainer.
     */
    private void loadWater(LocalGenContainer container) {
        //TODO support other liquids
        MaterialMap materialMap = MaterialMap.getInstance();
        container.getWaterTiles().forEach(position -> {
            liquidTiles.put(position, new LiquidTile(position, materialMap.getId("water"), 8));
        });
        //TODO add intensity based on river/brook water amount
        container.getWaterSources().forEach(position -> {
            liquidSources.put(position, new LiquidSource(position, materialMap.getId("water"), 1));
        });
    }

    /**
     * Once in turnDelay turns tries to move all liquids if possible
     */
    public void turn() {
        turnCount++;
        if (turnCount == turnDelay) {
            turnCount = 0;
            liquidTiles.keySet().forEach(this::turnTile);
        }
    }

    /**
     * Method for turning one tile of liquids.
     *
     * @param position position of tile.
     */
    private void turnTile(Position position) {
        LiquidTile liquidTile = liquidTiles.get(position);
        Position position1 = findTileToSpread(position);
        if (position1 != null) {
            // spread
        }
    }

    /**
     * Observes environment for LiquidTile and chooses new position for it to flow.
     * Liquids firstly try to fall down if are in the air,
     * then if possible, they flow or fall to lower level,
     * then spread on the same level.
     *
     * @param position
     * @return
     */
    private Position findTileToSpread(Position position) {
        Position lowerPos = new Position(position.getX(), position.getY(), position.getZ() - 1);
        if (localMap.inMap(lowerPos) &&
                localMap.getBlockType(position) == BlockTypesEnum.SPACE.getCode() &&
                localMap.isFlyPassable(lowerPos)) { // check to flow lower
            return lowerPos;
        }
        if() {
            ArrayList<Position> positions = observeTilesAround(lowerPos);
            if (!positions.isEmpty()) { // flow lower
                return positions.get(random.nextInt(positions.size()));
            }
        }


        if (localMap.inMap(lowerPos) && localMap.getBlockType(position) == BlockTypesEnum.SPACE.getCode()) { // check to flow lower
            if (localMap.isFlyPassable(lowerPos)) {
                return lowerPos; // fall down
            }
            ArrayList<Position> positions = observeTilesAround(lowerPos);
            if (!positions.isEmpty()) { // flow lower
                return positions.get(random.nextInt(positions.size()));
            }
        }
        ArrayList<Position> positions = observeTilesAround(position);
        if (!positions.isEmpty()) {
            return positions.get(random.nextInt(positions.size())); // flow on same level
        }
        return null;
    }

    private ArrayList<Position> observeTilesAround(Position position) {
        ArrayList<Position> positions = new ArrayList<>();
        for (int x = position.getX() - 1; x < position.getX() + 2; x++) {
            for (int y = position.getY() - 1; y < position.getY() + 2; y++) {
                if (!(x == 0 && y == 0) && // not same point
                        localMap.inMap(x, y, position.getZ()) &&
                        localMap.isFlyPassable(x, y, position.getZ())) { // can take liquid
                    positions.add(new Position(x, y, position.getZ()));
                }
            }
        }
        return positions;
    }

    public void initLiquidsToMap() {
        liquidTiles.values().forEach(liquidTile -> {
            Position position = liquidTile.position;
            localMap.setFlooding(position.getX(), position.getY(), position.getZ(), liquidTile.amount);
        });
    }

    private class LiquidTile {
        Position position;
        int liquid;
        int amount;

        public LiquidTile(Position position, int liquid, int amount) {
            this.position = position;
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
