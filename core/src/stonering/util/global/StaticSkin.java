package stonering.util.global;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import stonering.widget.BackgroundGenerator;

/**
 * @author Alexander Kuzyakov on 16.10.2017.
 * <p>
 * General class for access to Skin.
 */
public class StaticSkin {
    public static final Color background = new Color(85 / 256f, 85 / 256f, 85 / 256f, 1);
    public static final Color shade = new Color(120 / 256f, 120 / 256f, 120 / 256f, 0.25f);
    public static final Color backgroundFocused = new Color(115 / 256f, 115 / 256f, 115 / 256f, 1);
    public static final Color element = new Color(143 / 256f, 143 / 256f, 143 / 256f, 1);
    public static final Color elementFocused = new Color(170 / 256f, 159 / 256f, 111 / 256f, 1);
    public static final Color fontColor = new Color(240 / 256f, 240 / 256f, 240 / 256f, 1);

    public static final BackgroundGenerator generator = new BackgroundGenerator();

    private static Skin skin =
            new Skin(new FileHandle("ui_skin/uiskin.json"),
                    new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas")));

    public static Skin getSkin() {
        return skin;
    }
}