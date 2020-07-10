package stonering.game.model.system.liquid;

import static stonering.enums.blocks.BlockTypeEnum.*;
import static stonering.enums.blocks.BlockTypeEnum.WALL;
import static stonering.util.geometry.PositionUtil.*;

import java.util.*;
import java.util.stream.Collectors;

import com.sun.istack.Nullable;

import stonering.entity.Entity;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.game.model.system.EntitySystem;
import stonering.game.model.system.UtilitySystem;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 19.06.2020.
 */
public class LiquidMovingSystem extends UtilitySystem {
    private LiquidContainer container;
    private LocalMap localMap;
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
        localMap = GameMvc.model().get(LocalMap.class);
        turnCount++;
        if (turnCount == turnDelay) {
            turnCount = 0;
            updateLiquids();
        }
    }

    private void updateLiquids() {
        container.liquidTiles.entrySet().removeIf(entry -> entry.getValue().amount == 0); // remove empty liquid tiles

        container.liquidTiles.entrySet().stream()
                .filter(entry -> !entry.getValue().stable)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .forEach(pos -> {
                    if (!tryFallDown(pos) && !tryFallOver(pos) && !tryFlowToSide(pos)) container.getTile(pos).stable = true; // tile did not fall
                });

        container.liquidSources.values().stream() // generate liquid in sources
                .map(source -> source.position)
                .filter(position -> container.getAmount(position) < 7)
                .collect(Collectors.toList())
                .forEach(position -> createLiquidDelta(null, position, 1));
    }

    private boolean tryFallDown(Position position) {
        if (position.z <= 0) return false;
        Position lowerPosition = Position.add(position, 0, 0, -1);
        BlockTypeEnum currentType = localMap.blockType.getEnumValue(position);
        BlockTypeEnum lowerType = localMap.blockType.getEnumValue(lowerPosition);
        if ((currentType == SPACE || currentType == DOWNSTAIRS) && lowerType != WALL)
            if (container.getAmount(lowerPosition) < MAX_AMOUNT) { // can fall lower
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
                .filter(pos -> container.getAmount(pos) < MAX_AMOUNT) // tile not full
                .collect(Collectors.toList());
        if (positions.isEmpty()) return false;
        Position foundPosition = positions.size() == 1
                ? positions.get(0)
                : positions.get(random.nextInt(positions.size()));
        createLiquidDelta(position, foundPosition, 1);
        return true;
    }

    private boolean tryFlowToSide(Position position) {
        int currentAmount = container.getAmount(position);
        if (currentAmount < 2) return false;
        List<Position> positions = allNeighbourDeltas.stream()
                .map(pos -> Position.add(position, pos)) // add deltas
                .filter(localMap::inMap) // in map
                .filter(pos -> localMap.blockType.getEnumValue(pos) != WALL) // non wall
                .filter(pos -> container.getAmount(pos) < currentAmount) // not full
                .collect(Collectors.toList());
        if (positions.isEmpty()) return false;
        Position foundPosition = positions.get(random.nextInt(positions.size()));
        createLiquidDelta(position, foundPosition, 1);
        allNeighbourDeltas.stream()
                .map(pos -> Position.add(position, pos)) // add deltas
                .map(container::getTile)
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
                int amount = container.getAmount(pos);
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
        if (to != null && localMap.inMap(to)) container.setAmount(to, container.getAmount(to) + amount);
        if (from != null && localMap.inMap(from)) container.setAmount(from, container.getAmount(from) - amount);
    }
}
