package stonering.global;

import java.io.File;

/**
 * Created by Alexander on 06.08.2017.
 */
public class FileLoader {
    private final String MATERIALS_PATH = "resources/materials.json.json";

    public File getMaterialsFile() {
        return new File(MATERIALS_PATH);
    }
}
