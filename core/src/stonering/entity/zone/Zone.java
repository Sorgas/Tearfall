package stonering.entity.zone;

import stonering.entity.Entity;
import stonering.enums.ZoneTypesEnum;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Zone manages it's tiles and creates {@link stonering.entity.job.Task}.
 * Created through {@link ZoneTypesEnum}
 *
 * @author Alexander on 04.03.2019.
 */
public abstract class Zone extends Entity implements Cloneable {
    public String name;
    public final ZoneTypesEnum type;
    public final Set<Position> tiles;

    public Zone(String name, ZoneTypesEnum type) {
        this(name, type, new HashSet<>());
    }

    public Zone(String name,  ZoneTypesEnum type, Set<Position> tiles) {
        this.name = name;
        this.type = type;
        this.tiles = tiles;
    }
}
