package stonering.entity.local.zone;

import stonering.util.geometry.Area;

import java.util.ArrayList;

/**
 * Zone is area of local map. Can consist of multiple rectangles.
 *
 * @author Alexander on 04.03.2019.
 */
public class Zone {
    private ArrayList<Area> areas;

    public Zone(Area area) {
        areas = new ArrayList<>(1);
        areas.add(area);
    }
}
