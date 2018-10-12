package stonering.generators.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.global.utils.Position;
import stonering.entity.local.building.Building;
import stonering.utils.global.FileLoader;

/**
 * @author Alexander Kuzyakov on 07.12.2017.
 *
 * Generates BuildingType entity from descriptors
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
        return generateBuildinqQ(buildingJson);
    }

    private JsonValue findBuilding(String name) {
        for (JsonValue t: buildings) {
            if(t.getString("title").equals(name)) return t;
        }
        return null;
    }

    private Building generateBuildinqQ(JsonValue template) {
        Building building = new Building(new Position(0,0,0));
        building.setName(template.getString("title"));
        return building;
    }
}
