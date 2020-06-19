package stonering.game.model.tilemaps;

import stonering.game.model.system.ModelComponent;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains resolved sprite data of blocks as they are updated rarely.
 * Ramps are main reason, because their resolving require observation of neighbour tiles.
 *
 * @author Alexander Kuzyakov on 02.08.2017.
 */
public class LocalTileMap implements ModelComponent {
    private transient Map<Position, Position> map; // x, y, color(post MVP)
    private Position zeroTriple;
    private Position cachePosition;

    public LocalTileMap() {
        this.map = new HashMap<>();
        cachePosition = new Position();
        zeroTriple = new Position(0, 0, 0);
    }

    public void setTile(int x, int y, int z, int atlasX, int atlasY, int color) {
        setTile(cachePosition.set(x, y, z), atlasX, atlasY, color);
    }

    public void setTile(Position pos, int atlasX, int atlasY, int color) {
        map.computeIfAbsent(pos.clone(), pos1 -> new Position()).set(atlasX, atlasY, color);
    }

    public void removeTile(int x, int y, int z) {
        map.remove(cachePosition.set(x, y, z));
    }

    public void removeTile(Position position) {
        map.remove(position);
    }

    public Position get(int x, int y, int z) {
        return map.getOrDefault(cachePosition.set(x, y, z), zeroTriple);
    }

    public Position get(Position position) {
        return map.get(position);
    }
}
