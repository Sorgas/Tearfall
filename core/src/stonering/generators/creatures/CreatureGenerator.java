package stonering.generators.creatures;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import stonering.global.utils.Position;
import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.unit.aspects.MovementAspect;
import stonering.objects.local_actors.unit.aspects.PlanningAspect;
import stonering.objects.local_actors.unit.Unit;
import stonering.utils.global.FileLoader;

/**
 * Created by Alexander on 03.12.2017.
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
        Unit unit = new Unit(new Position(0, 0, 0));
        unit.addAspect(genarateBodyAspect(unit, creature));
        unit.addAspect(generatePlanningAspect(unit));
        unit.addAspect(generateMovementAspect(unit));
        return unit;
    }

    private Aspect generateMovementAspect(Unit unit) {
        return new MovementAspect(unit);
    }

    private Aspect genarateBodyAspect(Unit unit, JsonValue creature) {
        return bodyGenerator.generateBody(creature);
    }

    private Aspect generatePlanningAspect(Unit unit) {
        return new PlanningAspect(unit);
    }
}
