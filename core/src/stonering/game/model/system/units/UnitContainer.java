package stonering.game.model.system.units;

import com.badlogic.gdx.math.Vector3;
import stonering.enums.time.TimeUnitEnum;
import stonering.game.GameMvc;
import stonering.game.model.system.EntityContainer;
import stonering.game.model.system.tasks.CreaturePlanningSystem;
import stonering.game.model.system.tasks.CreatureTaskPerformingSystem;
import stonering.game.model.system.tasks.TaskContainer;
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
    Map<Position, List<Unit>> unitsMap;
    public final CreatureNeedSystem needSystem;
    public final CreatureBuffSystem buffSystem;
    public final CreatureHealthSystem healthSystem;
    public final CreatureMovementSystem movementSystem;
    public final CreaturePlanningSystem planningSystem;
    public final CreatureTaskPerformingSystem taskSystem;

    private Position cachePosition; // used for faster getting unit from map

    public UnitContainer() {
        cachePosition = new Position();
        unitsMap = new HashMap<>();
        needSystem = new CreatureNeedSystem();
        buffSystem = new CreatureBuffSystem();
        healthSystem = new CreatureHealthSystem();
        movementSystem = new CreatureMovementSystem();
        planningSystem = new CreaturePlanningSystem();
        taskSystem = new CreatureTaskPerformingSystem();
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

    public void updateUnitPosiiton(Unit unit, Vector3 vector) {
        removeUnitFromMap(unit);
        unit.setPosition(vector);
        addUnitToMap(unit);
    }

    public void updateUnitPosiiton(Unit unit, Position position) {
        removeUnitFromMap(unit);
        unit.setPosition(position);
        addUnitToMap(unit);
    }

    private void addUnitToMap(Unit unit) {
        Position position = unit.position;
        if (!unitsMap.containsKey(position)) unitsMap.put(position, new ArrayList<>());
        unitsMap.get(position).add(unit);
    }

    private void removeUnitFromMap(Unit unit) {
        List<Unit> unitsInOldPosition = unitsMap.get(unit.position);
        unitsInOldPosition.remove(unit);
        if (unitsInOldPosition.isEmpty()) unitsMap.remove(unit.position);
    }

    /**
     * Calls turnUnit() for all units.
     */
    public void turn() {
        for (Unit unit : entities) {
            unit.turn(); // TODO rework all aspects behaviour to systems
            healthSystem.updateCreatureHealth(unit);
            needSystem.updateNeedForCreature(unit);
            buffSystem.updateCreatureBuffs(unit);
            movementSystem.updateUnitPosition(unit);
        }
    }

    @Override
    public void turnUnit(TimeUnitEnum unit) {
        if(unit != TimeUnitEnum.MINUTE) return;
        for (Unit entity : entities) {
            healthSystem.updateCreatureHealth(entity);
            needSystem.updateNeedForCreature(entity);
            buffSystem.updateCreatureBuffs(entity);
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
        planningSystem.container = GameMvc.instance().getModel().get(TaskContainer.class);
    }
}
