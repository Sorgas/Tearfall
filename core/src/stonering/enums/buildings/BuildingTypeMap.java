package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.blocks.PassageEnum;
import stonering.enums.items.recipe.RecipeMap;
import stonering.enums.plants.PlantType;
import stonering.util.global.FileLoader;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Singleton map of building descriptors. Descriptors are stored by their names.
 *
 * @author Alexander Kuzyakov
 */
public class BuildingTypeMap {
    private static BuildingTypeMap instance;
    private HashMap<String, BuildingType> buildings;
    private HashMap<String, BuildingType> constructions;
    private Json json;

    private BuildingTypeMap() {
        buildings = new HashMap<>();
        constructions = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadTypesFileToMap(FileLoader.BUILDINGS_PATH, buildings);
        loadTypesFileToMap(FileLoader.FURNITURE_PATH, buildings);
        loadLists();
    }

    /**
     * Loads {@link PlantType} from given file into given file.
     */
    private void loadTypesFileToMap(String filePath, Map<String, BuildingType> map) {
        List<BuildingType> elements = json.fromJson(ArrayList.class, BuildingType.class, FileLoader.get(filePath));
        for (BuildingType buildingType : elements) {
            buildings.put(buildingType.building, buildingType);
            initSprites(buildingType); // adds offset to sprites
            parsePassage(buildingType);
        }
        Logger.LOADING.logDebug(map.keySet().size() + " loaded from " + filePath);
    }

    private void initSprites(BuildingType type) {
        for (int[] value : type.sprites) {
            value[0] += type.atlasXY[0];
            value[1] += type.atlasXY[1];
        }
    }

    private void parsePassage(BuildingType type) {
        type.passageArray = new PassageEnum[type.size[0]][type.size[1]];
        char[] chars = type.passage.toCharArray();
        for (int x = 0; x < type.size[0]; x++) {
            for (int y = 0; y < type.size[1]; y++) {
                int i = y * type.size[0] + x;
                type.passageArray[x][y] = PassageEnum.get(chars[i]);
            }
        }
    }

    /**
     * Loads lists of crafting recipes for building. First item in array should be building name.
     */
    private void loadLists() {
        Logger.LOADING.log("building recipes");
        RecipeMap recipeMap = RecipeMap.instance();
        ArrayList<ArrayList<String>> elements = json.fromJson(ArrayList.class, ArrayList.class, FileLoader.get(FileLoader.RECIPE_LISTS_PATH));
        for (List<String> recipeList : elements) {
            String buildingName = recipeList.remove(0);
            if(buildings.containsKey(buildingName)) {
                BuildingType type = buildings.get(buildingName);
                type.recipes = recipeList.stream().filter(recipeMap::hasRecipe).collect(Collectors.toList());
            }
        }
    }

    public static BuildingType getBuilding(String name) {
        return instance().buildings.get(name);
    }

    private static BuildingTypeMap instance() {
        if (instance == null) instance = new BuildingTypeMap();
        return instance;
    }
}
