package stonering.game.model.lists.units;

import stonering.game.model.lists.EntityContainer;
import stonering.util.geometry.Position;
import stonering.entity.unit.Unit;
import stonering.util.global.Initable;

import java.util.*;

/**
 * Contains all Units on localMap. Units are mapped with their positions(for faster rendering).
 * TODO add crud methods for unit.
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class UnitContainer extends EntityContainer<Unit> implements Initable {
    private Map<Position, List<Unit>> unitsMap;
    private CreatureNeedSystem needSystem;

    private Position cachePosition; // used for faster getting unit from map

    public UnitContainer() {
        cachePosition = new Position();
        unitsMap = new HashMap<>();
        needSystem = new CreatureNeedSystem();
    }

    /**
     * Add unit to container. Unit's position should be set.
     */
    public void addUnit(Unit unit) {
        addUnitToMap(unit);
        entities.add(unit);
        unit.init();
    }

    /**
     * Removes Unit from container. Unit's position should be valid.
     */
    private void removeUnit(Unit unit) {
        removeUnitFromMap(unit);
        entities.remove(unit);
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
     * Calls turn() for all unit.
     */
    public void turn() {
        for (Unit entity : entities) {
            entity.turn(); // TODO rework all aspects behaviour to systems
            needSystem.updateNeedForCreature(entity);
        }
    }

    /**
     * returns list of unit in given position. Returns null, if no unit exist in this position.
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
