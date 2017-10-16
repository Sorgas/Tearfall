package stonering.global;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Alexander on 16.10.2017.
 */
public class StaticSkin {
    private static com.badlogic.gdx.scenes.scene2d.ui.Skin skin =
            new com.badlogic.gdx.scenes.scene2d.ui.Skin(new FileHandle("ui_skin/uiskin.json"),
                    new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas")));

    public static com.badlogic.gdx.scenes.scene2d.ui.Skin getSkin() {
        return skin;
    }
}