package stonering.generators.creatures.needs;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.unit.need.NeedEnum;
import stonering.entity.unit.aspects.need.NeedState;
import stonering.enums.unit.CreatureType;
import stonering.entity.unit.aspects.need.NeedAspect;
import stonering.util.global.FileUtil;

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
        templates = reader.parse(FileUtil.get(FileUtil.BODY_TEMPLATE_PATH));
    }

    public NeedAspect generateNeedAspect(CreatureType type) {
        NeedAspect needAspect = new NeedAspect(null);
        type.bodyTemplate.needs.stream()
                .map(NeedEnum.map::get)
                .filter(Objects::nonNull)
                .forEach(need -> needAspect.needs.put(need, new NeedState(need)));
        return needAspect;
    }
}
