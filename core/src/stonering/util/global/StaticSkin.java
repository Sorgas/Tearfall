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
    // https://coolors.co/555555-737373-8f8f8f-aa9f6f-e2ce79-f0f0f0
    public static final Color shade = new Color(120 / 256f, 120 / 256f, 120 / 256f, 0.25f);

    public static final Color background = Color.valueOf("555555ff");
    public static final Color backgroundFocused = Color.valueOf("737373ff");
    public static final Color element = Color.valueOf("8f8f8fff");
    public static final Color elementFocused = Color.valueOf("AA9F6Fff");
    public static final Color button = Color.valueOf("E2CE79ff");
    public static final Color fontColor = Color.valueOf("f0f0f0ff");

    public static final BackgroundGenerator generator = new BackgroundGenerator();

    private static Skin skin =
            new Skin(new FileHandle("ui_skin/uiskin.json"),
                    new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas")));
    private static Skin skin2 =
            new Skin(new FileHandle("ui_skin/uiskin.json"),
                    new TextureAtlas(new FileHandle("ui_skin/uiskin.atlas")));

    public static Skin getSkin() {
        return skin;
    }

    public static Skin skin() {
        return skin2;
    }
}