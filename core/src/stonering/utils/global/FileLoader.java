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
    private final static String BUILDINGS_PATH = "resources/buildings.json";
    private final static String UI_TILES_PATH = "resources/ui_tiles.json";

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

    public static FileHandle getBuildingsFile() {
        return new FileHandle(BUILDINGS_PATH);
    }

    public static FileHandle getUiTilesFile() {
        return new FileHandle(UI_TILES_PATH);
    }

}
