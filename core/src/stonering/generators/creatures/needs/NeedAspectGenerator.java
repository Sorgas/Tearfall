package stonering.generators.creatures.needs;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.unit.aspects.needs.NeedEnum;
import stonering.enums.unit.CreatureType;
import stonering.entity.unit.aspects.needs.NeedsAspect;
import stonering.util.global.FileLoader;

import java.util.Objects;

/**
 * Generates needs for creatures.
 *
 * @author Alexander on 21.09.2018.
 */
public class NeedAspectGenerator {
    private Json json;
    private JsonReader reader;
    private JsonValue templates;

    public NeedAspectGenerator() {
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        templates = reader.parse(FileLoader.getFile(FileLoader.BODY_TEMPLATE_PATH));
    }

    public NeedsAspect generateNeedAspect(CreatureType type) {
        NeedsAspect needsAspect = new NeedsAspect(null);
        type.bodyTemplate.needs.stream().map(NeedEnum::get).filter(Objects::nonNull).forEach(needsAspect.needs::add);
        return needsAspect;
    }
}
