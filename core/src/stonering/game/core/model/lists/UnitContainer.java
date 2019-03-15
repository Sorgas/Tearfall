package stonering.game.core.model.lists;

import stonering.game.core.model.ModelComponent;
import stonering.game.core.model.Turnable;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.Unit;

import java.util.*;

/**
 * Contains all Units on localMap. Units are mapped with their positions(for faster rendering).
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class UnitContainer extends Turnable implements ModelComponent {
    private Map<Position, List<Unit>> unitsMap;

    private Position positionCache; // used for faster getting units from map

    public UnitContainer() {
        this(new ArrayList<>());
    }

    /**
     * Fills container from given collection.
     */
    public UnitContainer(List<Unit> units) {
        positionCache = new Position(0, 0, 0);
        unitsMap = new HashMap<>();
        for (Unit unit : units) {
            List<Unit> unitList = this.unitsMap.get(unit.getPosition());
            if (unitList == null) {
                unitsMap.put(unit.getPosition(), Arrays.asList(unit));
            } else {
                unitsMap.get(unit.getPosition()).add(unit);
            }
        }
    }

    /**
     * Calls turn() for all units.
     */
    public void turn() {
        for (List<Unit> unitList : unitsMap.values()) {
            unitList.forEach(Unit::turn);
        }
    }

    /**
     * returns list of units in given position. Returns null, if no units exist in this position.
     */
    public List<Unit> getUnitsInPosiiton(int x, int y, int z) {
        positionCache.x = x;
        positionCache.y = y;
        positionCache.z = z;
        return unitsMap.get(positionCache);
    }
}
