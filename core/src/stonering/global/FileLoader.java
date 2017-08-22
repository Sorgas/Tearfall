package stonering.global;

import java.io.File;

/**
 * Created by Alexander on 06.08.2017.
 */
public class FileLoader {
    private final static String MATERIALS_PATH = "resources/materials.json.json";

    public static File getMaterialsFile() {
        return new File(MATERIALS_PATH);
    }
}
