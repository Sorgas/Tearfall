package stonering.game.objects.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Alexander on 04.06.2017.
 */
public class Material {
    private String name;
    private Color color;
    private int id;

    public Material(String name, Color color, int id) {
        this.name = name;
        this.color = color;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
