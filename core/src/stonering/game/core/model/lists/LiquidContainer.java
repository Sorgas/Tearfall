package stonering.game.core.model.lists;

import stonering.game.core.model.LocalMap;
import stonering.global.utils.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * @author Alexander on 22.08.2018.
 */
public class LiquidContainer {
    private LocalMap localMap;
    private HashMap<Position, LiquidTile> liquidTiles;
    private Random random;
    private int turnDelay = 100;
    private int turnCount = 0;

    public LiquidContainer() {
        liquidTiles = new HashMap<>();
        random = new Random();
    }

    public void turn() {
        turnCount++;
        if (turnCount == turnDelay) {
            turnCount = 0;
            liquidTiles.keySet().forEach(this::turnTile);
        }
    }

    private void turnTile(Position position) {
        LiquidTile liquidTile = liquidTiles.get(position);
        if (liquidTile.amount > 1) { // spread

        }
    }

    private Position findTileToSpread(Position position) {
        Position lowerPos = new Position(position.getX(), position.getY(), position.getZ() - 1);
        if (localMap.inMap(lowerPos)) { // check to flow lower
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

    private class LiquidTile {
        Position position;
        int liquid;
        int amount;
    }

    public void setLocalMap(LocalMap localMap) {
        this.localMap = localMap;
    }
}
