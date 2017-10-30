package stonering.generators.creatures;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.aspects.BodyAspect;
import stonering.objects.creatures.BodyPart;

import java.util.ArrayList;

/**
 * Created by Alexander on 19.10.2017.
 */
public class BodyGenerator {
    private Json json;

    public BodyGenerator() {
        init();
    }

    private void init() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public void generateBody(String creatureDescriptor) {
        ArrayList<BodyPart> bodyParts = json.fromJson(ArrayList.class, BodyPart.class, creatureDescriptor);
        BodyAspect bodyAspect = new BodyAspect();
        for (BodyPart bodyPart: bodyParts) {
            bodyAspect.addBodyPart(bodyPart);
        }
    }
}
