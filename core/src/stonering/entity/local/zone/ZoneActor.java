package stonering.entity.local.zone;

import stonering.entity.local.AspectHolder;

/**
 * Zone actor manages zones and creates {@link stonering.entity.jobs.Task}.
 *
 * @author Alexander on 04.03.2019.
 */
public abstract class ZoneActor extends AspectHolder {
    private String name;
    private Zone zone;

    protected ZoneActor() {
        super(null); // zones have no particular position.
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
