package stonering.generators.creatures;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import stonering.global.utils.Position;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.aspects.EquipmentAspect;
import stonering.objects.local_actors.unit.aspects.MovementAspect;
import stonering.objects.local_actors.unit.aspects.PlanningAspect;
import stonering.objects.local_actors.unit.Unit;
import stonering.utils.global.FileLoader;

/**
 * @author Alexander Kuzyakov on 03.12.2017.
 *         <p>
 *         creates creatures from json files by specimen name
 */
public class CreatureGenerator {
    private JsonReader reader;
    private BodyGenerator bodyGenerator;
    private JsonValue creatures;

    public CreatureGenerator() {
        reader = new JsonReader();
        bodyGenerator = new BodyGenerator();
        creatures = reader.parse(FileLoader.getCreatureFile());
    }

    public Unit generateUnit(String specimen) {
        JsonValue creature = null;
        for (JsonValue c : creatures) {
            if (c.getString("title").equals(specimen)) {
                creature = c;
                break;
            }
        }
        Unit unit = new Unit(new Position(0, 0, 0)); //TODO change constructor.
        unit.addAspect(generateBodyAspect(unit, creature));
        unit.addAspect(generatePlanningAspect(unit));
        unit.addAspect(generateMovementAspect(unit));
        unit.addAspect(generateEquipmentAspect(unit));
        return unit;
    }

    private Aspect generateMovementAspect(Unit unit) {
        return new MovementAspect(unit);
    }

    private Aspect generateBodyAspect(Unit unit, JsonValue creature) {
        return bodyGenerator.generateBody(creature, unit);
    }

    private Aspect generatePlanningAspect(Unit unit) {
        return new PlanningAspect(unit);
    }

    private Aspect generateEquipmentAspect(Unit unit) {
        return new EquipmentAspect(unit);
    }
}
