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
    public final Map<Position, List<Unit>> unitsMap;
    public final NeedSystem needSystem;
    public final HealthSystem healthSystem;
    public final CreatureMovementSystem movementSystem;
    public final CreaturePlanningSystem planningSystem;
    public final CreatureActionPerformingSystem taskSystem;
    public final CreatureExperienceSystem experienceSystem;
    public final CreatureEquipmentSystem equipmentSystem;
    public final DiseaseSystem diseaseSystem;
    public final CreatureFallingSystem creatureFallingSystem;
    
    private Position cachePosition; // used for faster getting unit from map

    public UnitContainer() {
        cachePosition = new Position();
        unitsMap = new HashMap<>();
        addSystem(needSystem = new NeedSystem());
        addSystem(healthSystem = new HealthSystem());
        addSystem(movementSystem = new CreatureMovementSystem());
        addSystem(planningSystem = new CreaturePlanningSystem());
        addSystem(taskSystem = new CreatureActionPerformingSystem());
        addSystem(experienceSystem = new CreatureExperienceSystem());
        addSystem(equipmentSystem = new CreatureEquipmentSystem());
        addSystem(diseaseSystem = new DiseaseSystem());
        addSystem(creatureFallingSystem = new CreatureFallingSystem());
    }

    @Override
    public void add(Unit unit) {
        super.add(unit);
        putUnitToMap(unit);
    }

    @Override
    public void remove(Unit unit) {
        super.remove(unit);
        removeUnitFromMap(unit);
    }

    public void updateUnitPosition(Unit unit, Vector3 vector) {
        removeUnitFromMap(unit);
        unit.setPosition(vector);
        putUnitToMap(unit);
    }

    public void updateUnitPosition(Unit unit, Position position) {
        removeUnitFromMap(unit);
        unit.setPosition(position);
        putUnitToMap(unit);
    }

    private void putUnitToMap(Unit unit) {
        unitsMap.computeIfAbsent(unit.position.clone(), pos -> new ArrayList<>()).add(unit);
    }

    private void removeUnitFromMap(Unit unit) {
        List<Unit> unitsInOldPosition = unitsMap.get(unit.position);
        unitsInOldPosition.remove(unit);
        if (unitsInOldPosition.isEmpty()) unitsMap.remove(unit.position);
    }

    public List<Unit> getUnitsInPosition(int x, int y, int z) {
        return getUnitsInPosition(cachePosition.set(x, y, z));
    }

    public List<Unit> getUnitsInPosition(Position position) {
        return unitsMap.getOrDefault(position, Collections.emptyList());
    }
}
