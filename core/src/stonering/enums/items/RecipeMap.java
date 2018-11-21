package stonering.enums.items;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import stonering.utils.global.FileLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Alexander on 19.11.2018.
 */
public class RecipeMap {
    private static RecipeMap instance;
    private HashMap<String, Recipe> recipes;
    private Json json;

    private RecipeMap() {
        recipes = new HashMap<>();
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        loadRecipes();
    }

    public static RecipeMap getInstance() {
        if (instance == null)
            instance = new RecipeMap();
        return instance;
    }

    private void loadRecipes() {
        System.out.println("loading buildings");
        ArrayList<Recipe> elements = json.fromJson(ArrayList.class, Recipe.class, FileLoader.getFile(FileLoader.RECIPE_LISTS_PATH));
        for (Recipe recipe : elements) {
            recipes.put(recipe.getName(), recipe);
        }
    }

    public boolean hasMaterial(String title) {
        return recipes.containsKey(title);
    }

    public Recipe getRecipe(String name) {
        return recipes.get(name);
    }
}
