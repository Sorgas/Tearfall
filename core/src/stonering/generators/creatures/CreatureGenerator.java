package stonering.generators.creatures;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import stonering.utils.global.FileLoader;

/**
 * Created by Alexander on 03.12.2017.
 */
public class CreatureGenerator {
    private JsonReader reader;
    private BodyGenerator bodyGenerator;

    public CreatureGenerator() {
        reader = new JsonReader();
        bodyGenerator = new BodyGenerator();
    }

    public void generateWildAnimal(String specimen) {
        JsonValue creatures = reader.parse(FileLoader.getCreatureFile());
        JsonValue creature = null;
        for (JsonValue c : creatures) {
            if(c.getString("title").equals(specimen)) {
                creature = c;
                break;
            }
        }
        bodyGenerator.generateBody(creature);
    }
}
