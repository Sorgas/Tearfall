package stonering.global;

import com.badlogic.gdx.files.FileHandle;

import java.io.File;

/**
 * Created by Alexander on 06.08.2017.
 */
public class FileLoader {
    private final static String MINERALS_PATH = "resources/minerals.json";

    public static FileHandle getMineralsFile() {
        return new FileHandle(MINERALS_PATH);
    }
}
