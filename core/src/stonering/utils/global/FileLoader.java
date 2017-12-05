package stonering.utils.global;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Alexander on 06.08.2017.
 */
public class FileLoader {
    private final static String MATERIALS_PATH = "resources/materials.json";
    private final static String TREES_PATH = "resources/trees.json";
    private final static String PLANTS_PATH = "resources/plants.json";
    private final static String BODY_TEMPLATE_PATH = "resources/body_templates.json";
    private final static String CREATURES_PATH = "resources/creatures.json";

    public static FileHandle getMaterialsFile() {
        return new FileHandle(MATERIALS_PATH);
    }

    public static FileHandle getTreesFile() {
        return new FileHandle(TREES_PATH);
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
}
