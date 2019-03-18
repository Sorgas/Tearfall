package stonering.entity.local.zone;

import stonering.entity.local.AspectHolder;

/**
 * Zone actor manages zones and creates {@link stonering.entity.jobs.Task}.
 * Created through {@link stonering.enums.ZoneTypesEnum}
 *
 * @author Alexander on 04.03.2019.
 */
public abstract class ZoneActor extends AspectHolder implements Cloneable {
    private String name;
    private Zone zone;

    public ZoneActor(String name, Zone zone) {
        super(null);
        this.name = name;
        this.zone = zone;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
