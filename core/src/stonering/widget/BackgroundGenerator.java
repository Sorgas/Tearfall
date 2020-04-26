package stonering.widget;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Generator for single-color 1-pixel drawables.
 *
 * @author Alexander on 24.04.2020
 */
public class BackgroundGenerator {

    public Drawable generate(Color color) {
        return generate(color.r, color.g, color.b, color.a);
    }

    public Drawable generate(float r, float g, float b, float a) {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(r, g, b, a));
        pixmap.fill();
        return new TextureRegionDrawable(new Texture(pixmap));
    }
}
