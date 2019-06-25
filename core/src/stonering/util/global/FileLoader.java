package stonering.util.global;

import com.badlogic.gdx.files.FileHandle;

/**
 * @author Alexander Kuzyakov on 06.08.2017.
 */
public class FileLoader {
    public final static String MATERIALS_PATH = "resources/materials.json";
    public final static String PLANTS_PATH = "resources/plants/plants.json";
    public final static String BODY_TEMPLATE_PATH = "resources/body_templates.json";
    public final static String CREATURES_PATH = "resources/creatures.json";
    public final static String BUILDINGS_PATH = "resources/buildings/buildings.json";
    public final static String CONSTRUCTIONS_PATH = "resources/buildings/constructions.json";
    public final static String FURNITURE_PATH = "resources/buildings/furniture.json";
    public final static String BLUEPRINTS_PATH = "resources/buildings/blueprints.json";
    public final static String UI_TILES_PATH = "resources/ui_tiles.json";
    public final static String ITEMTYPE_TAGS_PATH = "resources/itemtype_tags.json";
    public final static String ITEMS_PATH = "resources/items";
    public final static String RECIPE_LISTS_PATH = "resources/crafting/lists.json";
    public final static String RECIPES_PATH = "resources/crafting/recipes.json";
    public final static String REGIONS_PATH = "resources/ui_background/regions.json";
    public final static String TREES_PATH = "resources/plants/trees.json";
    public final static String SUBSTRATES_PATH = "resources/plants/substrates.json";

    public static FileHandle getFile(String path) {
        return new FileHandle(path);
    }
}
