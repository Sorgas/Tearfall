package stonering.game.model.system.unit;

import com.badlogic.gdx.math.Vector3;

import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.task.CreatureActionPerformingSystem;
import stonering.util.geometry.Position;
import stonering.entity.unit.Unit;

import java.util.*;

/**
 * Contains all Units on localMap. Units are mapped with their positions(for faster rendering).
 * TODO add crud methods for unit.
 * TODO rework all aspects behaviour to systems
 *
 * @author Alexander Kuzyakov on 03.12.2017.
 */
public class UnitContainer extends EntityContainer<Unit> {
    Map<Position, List<Unit>> unitsMap;
    public final NeedSystem needSystem;
    public final CreatureBuffSystem buffSystem;
    public final CreatureHealthSystem healthSystem;
    public final CreatureMovementSystem movementSystem;
    public final CreaturePlanningSystem planningSystem;
    public final CreatureActionPerformingSystem taskSystem;
    public final CreatureExperienceSystem experienceSystem;
    public final CreatureEquipmentSystem equipmentSystem;
    public final CreatureDiseaseSystem creatureDiseaseSystem;

    private Position cachePosition; // used for faster getting unit from map

    public UnitContainer() {
        cachePosition = new Position();
        unitsMap = new HashMap<>();
        put(needSystem = new NeedSystem());
        put(buffSystem = new CreatureBuffSystem());
        put(healthSystem = new CreatureHealthSystem());
        put(movementSystem = new CreatureMovementSystem());
        put(planningSystem = new CreaturePlanningSystem());
        put(taskSystem = new CreatureActionPerformingSystem());
        put(experienceSystem = new CreatureExperienceSystem());
        put(equipmentSystem = new CreatureEquipmentSystem());
        put(creatureDiseaseSystem = new CreatureDiseaseSystem());
    }

    /**
     * Add unit to container. Unit's position should be set.
     */
    public void addUnit(Unit unit) {
        addUnitToMap(unit);
        objects.add(unit);
    }

    /**
     * Removes Unit from container. Unit's position should be valid.
     */
    private void removeUnit(Unit unit) {
        removeUnitFromMap(unit);
        objects.remove(unit);
    }

    public void updateUnitPosition(Unit unit, Vector3 vector) {
        removeUnitFromMap(unit);
        unit.setPosition(vector);
        addUnitToMap(unit);
    }

    public void updateUnitPosition(Unit unit, Position position) {
        removeUnitFromMap(unit);
        unit.setPosition(position);
        addUnitToMap(unit);
    }

    private void addUnitToMap(Unit unit) {
        Position position = unit.position.clone();
        if (!unitsMap.containsKey(position)) unitsMap.put(position, new ArrayList<>());
        unitsMap.get(position).add(unit);
    }

    private void removeUnitFromMap(Unit unit) {
        List<Unit> unitsInOldPosition = unitsMap.get(unit.position);
        unitsInOldPosition.remove(unit);
        if (unitsInOldPosition.isEmpty()) unitsMap.remove(unit.position);
    }

    /**
     * returns list of unit in given position. Returns null, if no unit exist in this position.
     */
    public List<Unit> getUnitsInPosition(int x, int y, int z) {
        return getUnitsInPosition(cachePosition.set(x, y, z));
    }

    public List<Unit> getUnitsInPosition(Position position) {
        return unitsMap.getOrDefault(position, Collections.emptyList());
    }
}
