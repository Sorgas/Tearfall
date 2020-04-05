package stonering.util.global;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * @author Alexander Kuzyakov on 16.10.2017.
 *
 * General class for access to Skin+
 */
public class StaticSkin {
    private static Skin skin =
            new Skin(new FileHandle("ui_skin/uiskin.json"),
                    new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas")));

    public static Skin getSkin() {
        return skin;
    }
}