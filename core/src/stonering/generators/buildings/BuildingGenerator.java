package stonering.generators.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.entity.local.building.BuildingType;
import stonering.enums.buildings.BuildingMap;
import stonering.global.utils.Position;
import stonering.entity.local.building.Building;

/**
 * @author Alexander Kuzyakov on 07.12.2017.
 *
 * Generates BuildingType entity from descriptors
 */
public class BuildingGenerator {
    private Json json;
    private JsonReader reader;
    private JsonValue buildings;
    private BuildingMap buildingMap;

    public BuildingGenerator() {
        init();
    }

    private void init() {
        reader = new JsonReader();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        buildingMap = BuildingMap.getInstance();
    }

    public Building generateBuilding(String name) {
        BuildingType type = buildingMap.getBuilding(name);
        Building building = new Building(new Position(0,0,0));
        building.setName(type.getTitle());
        return building;
    }
}
