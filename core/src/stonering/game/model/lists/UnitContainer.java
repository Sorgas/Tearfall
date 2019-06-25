package stonering.game.model.lists;

import com.badlogic.gdx.utils.Array;
import stonering.game.model.ModelComponent;
import stonering.game.model.Turnable;
import stonering.util.geometry.Position;
import stonering.entity.local.unit.Unit;
import stonering.util.global.CompatibleArray;
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
    private Array<Unit> units; // list for turning

    private Position cachePosition; // used for faster getting units from map

    public UnitContainer() {
        cachePosition = new Position();
        unitsMap = new HashMap<>();
        units = new Array();
    }

    /**
     * Add unit to container. Unit's position should be set.
     */
    public void addUnit(Unit unit) {
        addUnitToMap(unit);
        units.add(unit);
        unit.init();
    }

    /**
     * Removes Unit from container. Unit's position should be valid.
     */
    private void removeUnit(Unit unit) {
        removeUnitFromMap(unit);
        units.removeValue(unit, true);
    }

    /**
     * Moves unit to new position.
     */
    public void updateUnitPosiiton(Unit unit, Position position) {
        removeUnitFromMap(unit);
        unit.setPosition(position);
        addUnitToMap(unit);
    }

    private void addUnitToMap(Unit unit) {
        Position position = unit.getPosition();
        if (!unitsMap.containsKey(position)) unitsMap.put(position, new ArrayList<>());
        unitsMap.get(position).add(unit);
    }

    private void removeUnitFromMap(Unit unit) {
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
        return getUnitsInPosition(cachePosition.set(x,y,z));
    }

    public List<Unit> getUnitsInPosition(Position position) {
        return unitsMap.getOrDefault(position, Collections.emptyList());
    }


    @Override
    public void init() {
        unitsMap.values().forEach(units -> units.forEach(Unit::init));
    }
}
