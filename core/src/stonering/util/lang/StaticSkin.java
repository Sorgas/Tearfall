package stonering.util.lang;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import stonering.widget.BackgroundGenerator;

/**
 * @author Alexander Kuzyakov on 16.10.2017.
 * <p>
 * General class for access to Skin.
 */
public class StaticSkin {
    // https://coolors.co/555555-737373-8f8f8f-aa9f6f-e2ce79-f0f0f0
    public static final Color shade = new Color(120 / 256f, 120 / 256f, 120 / 256f, 0.25f);
    public static final Color ui_shade = new Color(100 / 256f, 100 / 256f, 100 / 256f, 0.6f);

    public static final Color background = Color.valueOf("555555ff"); // grey 1
    public static final Color backgroundFocused = Color.valueOf("737373ff"); // grey 2
    public static final Color element = Color.valueOf("8f8f8fff"); // grey 3
    public static final Color elementFocused = Color.valueOf("b5b5b5ff"); // grey 4
    public static final Color button = Color.valueOf("C6B774ff"); // dark yellow
    public static final Color button2 = Color.valueOf("E2CE79ff"); // yellow
    public static final Color fontColor = Color.valueOf("f0f0f0ff"); // white
    public static final Color buttonShade = Color.valueOf("8080808"); // grey 50% transparency

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

    public static Drawable getColorDrawable(Color color) {
        return generator.generate(color);
    }
}