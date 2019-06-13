package stonering.entity.local.zone;

import stonering.entity.local.Entity;
import stonering.enums.ZoneTypesEnum;
import stonering.util.geometry.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * Zone manages it's tiles and creates {@link stonering.entity.job.Task}.
 * Created through {@link stonering.enums.ZoneTypesEnum}
 *
 * @author Alexander on 04.03.2019.
 */
public abstract class Zone extends Entity implements Cloneable {
    protected String name;
    protected Set<Position> tiles;
    protected ZoneTypesEnum type;

    public Zone(String name) {
        this(name, new HashSet<>());
    }

    public Zone(String name, Set<Position> tiles) {
        super(null);
        this.name = name;
        this.tiles = tiles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Position> getTiles() {
        return tiles;
    }

    public ZoneTypesEnum getType() {
        return type;
    }
}
