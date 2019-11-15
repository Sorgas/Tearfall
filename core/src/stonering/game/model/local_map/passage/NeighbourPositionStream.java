package stonering.game.model.local_map.passage;

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
    private PassageMap passage;
    private Position center;

    public NeighbourPositionStream(Position center, PassageMap passage) {
        this.passage = passage;
        this.center = center;
        Set<Position> neighbours = new HashSet<>();
        for (int x = center.x - 1; x < center.x + 2; x++) {
            for (int y = center.y - 1; y < center.y + 2; y++) {
                for (int z = center.z - 1; z < center.z + 2; z++) {
                    Position position = new Position(x, y, z);
                    if(!position.equals(center)) neighbours.add(position);
                }
            }
        }
        stream = neighbours.stream();
    }

    public NeighbourPositionStream filterByReachability() {
        stream = stream.filter(position -> passage.hasPathBetweenNeighbours(position, center));
        return this;
    }

    public NeighbourPositionStream filterByArea(int value) {
        stream = stream.filter(position -> passage.area.get(position) == value);
        return this;
    }
}
