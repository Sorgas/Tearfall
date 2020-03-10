package stonering.enums.buildings;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.enums.blocks.PassageEnum;
import stonering.enums.items.recipe.RecipeMap;
import stonering.util.global.FileUtil;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Singleton map of building descriptors. Descriptors are stored by their names.
 *
 * @author Alexander Kuzyakov
 */
public class BuildingTypeMap {
    private static BuildingTypeMap instance;
    private HashMap<String, BuildingType> buildings;
    private Json json;

    private BuildingTypeMap() {
        buildings = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        FileUtil.iterate(FileUtil.BUILDINGS_PATH, file -> {
            int counter = 0;
            List<BuildingType> elements = json.fromJson(ArrayList.class, BuildingType.class, file);
            for (BuildingType buildingType : elements) {
                buildings.put(buildingType.building, buildingType);
                initSprites(buildingType); // adds offset to sprites
                parsePassage(buildingType); // parse passage string into map of passage
                counter ++;
            }
            Logger.LOADING.logDebug(counter + " loaded from " + file.path());
        });
        loadLists();
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
        FileUtil.iterate(FileUtil.RECIPE_LISTS_PATH, file -> {
            ArrayList<ArrayList<String>> elements = json.fromJson(ArrayList.class, ArrayList.class, file);
            for (List<String> recipeList : elements) {
                String buildingName = recipeList.remove(0);
                if(buildings.containsKey(buildingName)) {
                    BuildingType type = buildings.get(buildingName);
                    type.recipes = recipeList.stream().filter(recipeMap::hasRecipe).collect(Collectors.toList());
                } else {
                    Logger.LOADING.logWarn("Recipes for unknown building " + buildingName + " ignored.");
                }
            }
        });
        ArrayList<ArrayList<String>> elements = json.fromJson(ArrayList.class, ArrayList.class, FileUtil.get(FileUtil.RECIPE_LISTS_PATH));
        for (List<String> recipeList : elements) {
            String buildingName = recipeList.remove(0);
            if(buildings.containsKey(buildingName)) {
                BuildingType type = buildings.get(buildingName);
                type.recipes = recipeList.stream().filter(recipeMap::hasRecipe).collect(Collectors.toList());
            } else {
                Logger.LOADING.logWarn("Recipes for unknown building " + buildingName + " ignored.");
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
