package stonering.util.lang;

import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility for working with files.
 *
 * @author Alexander Kuzyakov on 06.08.2017.
 */
public class FileUtil {
    public final static String MATERIALS_PATH = "resources/materials";
    public final static String CREATURES_PATH = "resources/creatures/creatures.json";
    public final static String DISEASES_PATH = "resources/creatures/diseases.json";
    public final static String BODY_TEMPLATE_PATH = "resources/creatures/body_templates.json";
    public final static String SKILLS_PATH = "resources/creatures/skills.json";
    public final static String UI_TILES_PATH = "resources/ui_tiles.json";
    public final static String ITEMTYPE_TAGS_PATH = "resources/itemtype_tags.json";
    public final static String ITEMS_PATH = "resources/items";
    public final static String RECIPE_LISTS_PATH = "resources/crafting/lists.json";
    public final static String RECIPES_PATH = "resources/crafting/recipes.json";
    public final static String DRAWABLE_DESCRIPTORS_PATH = "resources/drawable/";
    public final static String SEASONS_PATH = "resources/seasons.json";
    public final static String JOBS_PATH = "resources/creatures/jobs.json";

    public final static String PLANTS_PATH = "resources/plants/plants";
    public final static String TREES_PATH = "resources/plants/trees";
    public final static String SUBSTRATES_PATH = "resources/plants/substrates";

    public final static String BLUEPRINTS_PATH = "resources/blueprints/blueprints.json";
    public final static String BUILDINGS_PATH = "resources/buildings";

    public static FileHandle get(String path) {
        return new FileHandle(path);
    }

    /**
     * Applies some logic to file or all files in directory tree,
     *
     * @param path     path of file or catalogue
     * @param consumer logic function
     */
    public static void iterate(String path, Consumer<FileHandle> consumer) {
        FileHandle root = get(path);
        List<FileHandle> files = new ArrayList<>();
        files.add(root);
        while (!files.isEmpty()) {
            FileHandle file = files.remove(0);
            if (file.isDirectory()) {
                files.addAll(Arrays.asList(file.list()));
            } else {
                consumer.accept(file);
            }
        }
    }
}
