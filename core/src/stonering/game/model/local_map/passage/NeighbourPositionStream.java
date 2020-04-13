package stonering.game.model.local_map.passage;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Stream wrapper for convenient work with tiles around another tile.
 *
 * @author Alexander on 14.11.2019.
 */
public class NeighbourPositionStream {
    public Stream<Position> stream;
    private Position center;
    private PassageMap passageMap;
    private LocalMap localMap;

    public NeighbourPositionStream(Position center) {
        this.center = center;
        localMap = GameMvc.model().get(LocalMap.class);
        passageMap = localMap.passageMap;
        Set<Position> neighbours = new HashSet<>();
        for (int x = center.x - 1; x < center.x + 2; x++) {
            for (int y = center.y - 1; y < center.y + 2; y++) {
                for (int z = center.z - 1; z < center.z + 2; z++) {
                    Position position = new Position(x, y, z);
                    if(!position.equals(center)) neighbours.add(position);
                }
            }
        }
        stream = neighbours.stream().filter(localMap::inMap);
    }

    /**
     * Gives neighbours by x and y, having provided z
     */
    public NeighbourPositionStream(Int2dBounds bounds, int z) {
        localMap = GameMvc.model().get(LocalMap.class);
        passageMap = localMap.passageMap;
        Set<Position> neighbours = new HashSet<>();
        for (int x = bounds.minX - 1; x < bounds.maxX + 2; x++) {
            neighbours.add(new Position(x, bounds.minY - 1, z));
            neighbours.add(new Position(x, bounds.maxY + 1, z));
        }
        for (int y = bounds.minY - 1; y < bounds.maxY + 2; y++) {
            neighbours.add(new Position(bounds.minX - 1, y, z));
            neighbours.add(new Position(bounds.maxX + 1, y, z));
        }
        stream = neighbours.stream().filter(localMap::inMap);
    }

    /**
     * Filters all tiles where walking creature cannot step into.
     * Clears all, if center tile is not passable.
     */
    public NeighbourPositionStream filterByPassability() {
        stream = stream.filter(position -> passageMap.hasPathBetweenNeighbours(position, center));
        return this;
    }

    /**
     * Considers center tile to have given type. Used for checking during building.
     */
    public NeighbourPositionStream filterByAccessibilityWithFutureTile(BlockTypeEnum type) {
        stream = stream.filter(position -> passageMap.tileIsAccessibleFromNeighbour(center, position, type));
        return this;
    }

    public NeighbourPositionStream filterNotInArea(int value) {
        stream = stream.filter(position -> passageMap.area.get(position) != value);
        return this;
    }

    public NeighbourPositionStream filterInArea(int value) {
        stream = stream.filter(position -> passageMap.area.get(position) == value);
        return this;
    }

    public NeighbourPositionStream filterByPassage(PassageEnum passage) {
        stream = stream.filter(position -> passageMap.passage.get(position) == passage.VALUE);
        return this;
    }

    public NeighbourPositionStream filterByBlockType(BlockTypeEnum type) {
        stream = stream.filter(position -> localMap.getBlockType(position) == type.CODE);
        return this;
    }

    public NeighbourPositionStream filterSameZLevel() {
        stream = stream.filter(position -> position.z == center.z);
        return this;
    }
}
