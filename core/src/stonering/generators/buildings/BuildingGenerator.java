package stonering.generators.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.objects.local_actors.building.BuildingBlock;
import stonering.objects.local_actors.unit.BodyPart;
import stonering.objects.local_actors.building.Building;
import stonering.utils.global.FileLoader;

/**
 * Created by Alexander on 07.12.2017.
 */
public class BuildingGenerator {
    private Json json;
    private JsonReader reader;
    private JsonValue buildings;


    public BuildingGenerator() {
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        buildings = reader.parse(FileLoader.getBuildingsFile());
    }

    public Building generateBuilding(String name) {
        JsonValue buildingJson = findBuilding(name);
        Building building = generateBuildinqQ(buildingJson);
        return building;
    }

    private JsonValue findBuilding(String name) {
        for (JsonValue t: buildings) {
            if(t.getString("title").equals(name)) return t;
        }
        return null;
    }

    private Building generateBuildinqQ(JsonValue template) {
        Building building = new Building();
        BuildingBlock buildingBlock = new BuildingBlock();
        buildingBlock.setAtlasX(0);
        building.setBlock(buildingBlock);
        building.setName(template.getString("title"));
        return building;
    }
}
