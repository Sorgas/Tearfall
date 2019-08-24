package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Descriptor of material. Tags are copied to item on creation.
 *
 * @author Alexander Kuzyakov on 04.06.2017.
 */

// STONE(0),
// SOIL(1),
// SAND(2),
// WOOD(3),
// BRICKS(5),
// PLANKS(6),
// GLASS(7),
// METAL(8);
public class Material {
    private int id;
    private String name;
    private ArrayList<String> tags;
    private float density;
    private HashMap<String, ArrayList<Object>> reactions; // other aspects
    private int value;
    private byte atlasY;
    private String colorCode;
    private Color color;

    public Material() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public HashMap<String, ArrayList<Object>> getReactions() {
        return reactions;
    }

    public void setReactions(HashMap<String, ArrayList<Object>> reactions) {
        this.reactions = reactions;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public byte getAtlasY() {
        return atlasY;
    }

    public void setAtlasY(byte atlasY) {
        this.atlasY = atlasY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        color = new Color(Integer.valueOf(colorCode));
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}