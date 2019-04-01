package stonering.game.model.lists;

import stonering.game.model.ModelComponent;
import stonering.game.model.Turnable;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.Unit;
import stonering.util.global.Initable;

import java.util.*;

/**
 * Contains all Units on localMap. Units are mapped with their positions(for faster rendering).
 * TODO add crud methods for units.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class UnitContainer extends Turnable implements ModelComponent, Initable {
    private Map<Position, List<Unit>> unitsMap;
    private List<Unit> units;

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
        this.units = new ArrayList<>(units);
        for (Unit unit : this.units) {
            putUnitToPosition(unit, unit.getPosition());
        }
    }

    private void putUnitToPosition(Unit unit, Position position) {
        List<Unit> unitList = this.unitsMap.get(unit.getPosition());
        if (unitList == null) unitsMap.put(position, new ArrayList<>());
        unitsMap.get(position).add(unit);
        unit.setPosition(position);
    }

    private void removeUnit(Unit unit) {
        List<Unit> unitsInOldPosition = unitsMap.get(unit.getPosition());
        unitsInOldPosition.remove(unit);
        if (unitsInOldPosition.isEmpty()) unitsMap.remove(unit.getPosition());
    }

    /**
     * Calls turn() for all units.
     */
    public void turn() {
        units.forEach(Unit::turn);
    }

    /**
     * returns list of units in given position. Returns null, if no units exist in this position.
     */
    public List<Unit> getUnitsInPosition(int x, int y, int z) {
        positionCache.x = x;
        positionCache.y = y;
        positionCache.z = z;
        return getUnitsInPosition(positionCache);
    }

    public List<Unit> getUnitsInPosition(Position position) {
        return unitsMap.getOrDefault(position, Collections.EMPTY_LIST);
    }


    @Override
    public void init() {
        units.forEach(Unit::init);
    }

    public void updateUnitPosiiton(Unit unit, Position newPosition) {
        removeUnit(unit);
        putUnitToPosition(unit, newPosition);
    }
}
