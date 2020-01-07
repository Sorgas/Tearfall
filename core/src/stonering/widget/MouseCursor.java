package stonering.widget;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Widget to be rendered instead of system mouse cursor.
 * Is an enum of textures, and can switch between them.
 *
 * @author Alexander on 07.01.2020
 */
public enum MouseCursor {
    REGULAR("sprites/mouse/regular.png");

    private final Texture TEXTURE;
    private static MouseCursor selectedCursor = REGULAR;
    private static float scale = 2f;

    MouseCursor(String path) {
        TEXTURE = new Texture(path);
    }

    public static void draw(Batch batch) {
        int x = Gdx.input.getX();
        int y = Gdx.input.getY();
        Texture texture = selectedCursor.TEXTURE;
        batch.draw(texture, x, Gdx.graphics.getHeight() - y - texture.getHeight() * scale, texture.getWidth() * scale, texture.getHeight() * scale);
    }

    public static void set(MouseCursor mouseCursor) {
        selectedCursor = mouseCursor;
    }

    public void set() {
        set(this);
    }
}
