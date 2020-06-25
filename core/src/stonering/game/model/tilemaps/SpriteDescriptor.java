package stonering.game.model.tilemaps;

import com.badlogic.gdx.graphics.Color;

/**
 * @author Alexander on 6/25/2020
 */
public class SpriteDescriptor {
    public int x;
    public int y;
    public Color color;

    public SpriteDescriptor(int x, int y, Color color) {
        set(x, y, color);
    }

    public SpriteDescriptor() {
    }

    public void set(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }
}
