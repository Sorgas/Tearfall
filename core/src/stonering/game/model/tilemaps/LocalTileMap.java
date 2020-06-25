package stonering.game.model.tilemaps;

import stonering.game.model.system.ModelComponent;
import stonering.util.geometry.Position;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;

/**
 * Contains resolved sprite data of blocks as they are updated rarely.
 * Ramps are main reason, because their resolving require observation of neighbour tiles.
 *
 * @author Alexander Kuzyakov on 02.08.2017.
 */
public class LocalTileMap implements ModelComponent {
    private transient Map<Position, SpriteDescriptor> map; // x, y, color(post MVP)
//    private transient Map<Position, SpriteDescriptor> zoneTiles;
    private Position cachePosition;

    public LocalTileMap() {
        this.map = new HashMap<>();
        cachePosition = new Position();
    }

    public void setTile(int x, int y, int z, int atlasX, int atlasY, Color color) {
        setTile(cachePosition.set(x, y, z), atlasX, atlasY, color);
    }

    public void setTile(Position pos, int atlasX, int atlasY, Color color) {
        map.computeIfAbsent(pos.clone(), pos1 -> new SpriteDescriptor()).set(atlasX, atlasY, color);
    }

    public void removeTile(int x, int y, int z) {
        map.remove(cachePosition.set(x, y, z));
    }

    public void removeTile(Position position) {
        map.remove(position);
    }

    public SpriteDescriptor get(int x, int y, int z) {
        return map.get(cachePosition.set(x, y, z));
    }

    public SpriteDescriptor get(Position position) {
        return map.get(position);
    }
}
