package stonering.game.model.system.liquid;

import static stonering.enums.blocks.BlockTypeEnum.*;
import static stonering.enums.blocks.BlockTypeEnum.WALL;
import static stonering.util.geometry.PositionUtil.*;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.istack.Nullable;

import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.UtilitySystem;
import stonering.util.geometry.Position;

/**
 * Moves liquid tiles and creates water in liquid sources.
 * Liquid can fall and spread to adjacent tile.
 * If liquid is on top of some filled tiles, it will find way through them and 'teleport' lower(U-tube behaviour).
 *
 * @author Alexander on 19.06.2020.
 */
public class LiquidMovingSystem extends UtilitySystem {
    private LiquidContainer container;
    private LocalMap map;
    private int turnDelay = 10;
    private int turnCount = 0;
    private final Random random;
    private final int MAX_AMOUNT = 7;

    public LiquidMovingSystem(LiquidContainer container) {
        this.container = container;
        random = new Random();
    }

    @Override
    public void update() {
        map = GameMvc.model().get(LocalMap.class);
        if (++turnCount >= turnDelay) {
            turnCount = 0;
            updateLiquids();
        }
    }

    private void updateLiquids() {
        container.liquidTiles.entrySet().removeIf(entry -> entry.getValue().amount == 0); // remove empty liquid tiles
        container.liquidTiles.entrySet().stream() // move liquid
                .filter(entry -> !entry.getValue().stable)
                .collect(Collectors.toList()) // avoids concurrent mod exception
                .forEach(entry -> {
                    Position pos = entry.getKey();
                    if (!tryFallDown(pos) && !trySpread(pos)) entry.getValue().stable = true; // call liquid movement in specific order
                });
        container.liquidSources.values().stream() // generate liquid in sources
                .map(source -> source.position)
                .filter(position -> map.blockType.get(position) != WALL.CODE)
                .filter(position -> container.getAmount(position) < 7)
                .collect(Collectors.toList())
                .forEach(position -> {
                    createLiquidDelta(null, position, 1);
                    container.getTile(position).stable = false;
                });
    }

    private boolean tryFallDown(Position from) {
        if (from.z <= 0 || tileHasFloor(map.blockType.get(from))) return false; // current tile contains floor and cannot pass liquid down
        Position lower = Position.add(from, 0, 0, -1);
        if (map.blockType.get(lower) == WALL.CODE) return false; // lower block cannot accept water
        if(container.getAmount(lower) < MAX_AMOUNT) {
            destabilizeAdjacentLiquid(from);
            return createLiquidDelta(from, lower, 1); // simple falling 
        } else {
            return tryTeleport(from, lower);
        }
    }

    private boolean tryTeleport(Position from, Position lower) {
        LiquidTile lowerTile = container.getTile(lower);
        if(lowerTile.tpStable) return false;
        Position to = findPositionToTeleport(from);
        if (to == null) {
            lowerTile.tpStable = true; // tile cannot pass liquid anymore
            return false;
        } else {
            destabilizeAdjacentLiquid(from);
            return createLiquidDelta(from, to, 1); // true
        }
    }
    
    /**
     * Finds positions to teleport liquid as regular flowing is too slow to handle waterfalls and U-shaped tubes.
     * Collects all filled tiles under given, collects neighbours of filled tiles(outflows).
     *
     * @return position of lowest and emptiest outflow.
     */
    private Position findPositionToTeleport(Position from) {
        Set<Position> open = new HashSet<>();
        Set<Position> closed = new HashSet<>();
        Set<Position> out = new HashSet<>();
        open.add(from);
        // collect all filled tiles and outflows
        while (!open.isEmpty()) {
            Position position = open.iterator().next();
            open.remove(position);
            position.neighbourStream(waterflow)
                    .filter(pos -> pos.z < from.z)
                    .filter(map::inMap)
                    .filter(pos -> !closed.contains(pos))
                    .filter(pos -> map.blockType.get(pos) != WALL.CODE)
                    .forEach(pos -> (container.getAmount(pos) == MAX_AMOUNT ? open : out).add(pos));  // add position to open set or outflows
            closed.add(position);
        }
        return out.stream()
                .min((pos1, pos2) -> pos1.z != pos2.z ? pos1.z - pos2.z : container.getAmount(pos1) - container.getAmount(pos2))
                .orElse(null);
    }

    private boolean tryFallOver(Position from) {
        if (from.z <= 0) return false;
        List<Position> lowerPositions = from.neighbourStream(fourNeighbour)
                .filter(map::inMap)
                .filter(pos -> tileHasFloor(map.blockType.get(pos))) // tile can pass liquid down
                .map(pos -> pos.add(0, 0, -1)) // get lower positions
                .filter(pos -> map.blockType.get(pos) != WALL.CODE) // tile can contain liquids
                .filter(pos -> container.getAmount(pos) < MAX_AMOUNT) // tile not full
                .collect(Collectors.toList());
        if (lowerPositions.isEmpty()) return false;
        destabilizeAdjacentLiquid(from); // liquid moved out, so adjacent tiles can flow into it
        Position foundPosition = lowerPositions.size() == 1
                ? lowerPositions.get(0)
                : lowerPositions.get(random.nextInt(lowerPositions.size()));
        return createLiquidDelta(from, foundPosition, 1);
    }

    private boolean trySpread(Position from) {
        int currentAmount = container.getAmount(from);
        if (currentAmount < 2) return false;
        List<Position> nearPositions = from.neighbourStream(allNeighbour) // find position to flow to
                .filter(map::inMap) // in map
                .filter(pos -> map.blockType.getEnumValue(pos) != WALL) // non wall
                .filter(pos -> container.getAmount(pos) < currentAmount) // not full
                .collect(Collectors.toList());
        if (nearPositions.isEmpty()) return false;
        Position to = nearPositions.get(nearPositions.size() == 1 ? 0 : random.nextInt(nearPositions.size()));
        destabilizeAdjacentLiquid(from); // liquid moved out, so adjacent tiles can flow into it
        return createLiquidDelta(from, to, 1); // move liquid
    }

    private boolean createLiquidDelta(@Nullable Position from, @Nullable Position to, int amount) {
        if (to != null && map.inMap(to)) container.setAmount(to, container.getAmount(to) + amount);
        if (from != null && map.inMap(from)) container.setAmount(from, container.getAmount(from) - amount);
        return true;
    }
    
    public void destabilizeAdjacentLiquid(Position center) {
        center.neighbourStream(waterflow)
                .map(container::getTile)
                .filter(Objects::nonNull)
                .forEach(tile -> tile.stable = false);
    }
    
    private boolean tileHasFloor(byte type) {
        return type != SPACE.CODE && type != DOWNSTAIRS.CODE && type != STAIRS.CODE; 
    }
}
