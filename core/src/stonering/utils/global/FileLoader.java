package stonering.utils.global;

import com.badlogic.gdx.files.FileHandle;

/**
 * @author Alexander Kuzyakov on 06.08.2017.
 */
public class FileLoader {
    private final static String MATERIALS_PATH = "resources/materials.json";
    private final static String PLANTS_PATH = "resources/plants.json";
    private final static String BODY_TEMPLATE_PATH = "resources/body_templates.json";
    private final static String CREATURES_PATH = "resources/creatures.json";
    private final static String BUILDINGS_PATH = "resources/buildings/buildings.json";
    private final static String UI_TILES_PATH = "resources/ui_tiles.json";
    private final static String ITEMTYPE_TAGS_PATH = "resources/itemtype_tags.json";
    private final static String ITEMS_PATH = "resources/items/mvp_items.json";
    private final static String RECIPES_PATH = "resources/items/mvp_recipes.json";

    public static FileHandle getMaterialsFile() {
        return new FileHandle(MATERIALS_PATH);
    }

    public static FileHandle getPlantsFile() {
        return new FileHandle(PLANTS_PATH);
    }

    public static FileHandle getCreatureFile() {
        return new FileHandle(CREATURES_PATH);
    }

    public static FileHandle getBodyTemplateFile() {
        return new FileHandle(BODY_TEMPLATE_PATH);
    }

    public static FileHandle getBuildingsFile() {
        return new FileHandle(BUILDINGS_PATH);
    }

    public static FileHandle getUiTilesFile() {
        return new FileHandle(UI_TILES_PATH);
    }

    public static FileHandle getItemTypeTags() {
        return new FileHandle(ITEMTYPE_TAGS_PATH);
    }

    public static FileHandle getItemsFile() {
        return new FileHandle(ITEMS_PATH);
    }
}
