package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Descriptor of material.
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
    private ArrayList<String> types;
    private float density;
    private HashMap<String, ArrayList<Object>> reactions; // other aspects
    private int value;
    private byte atlasY;
    private Color color;

    public Material() {}

    public Material(String name, Color color, byte atlasY, int id) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.atlasY = atlasY;
    }

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

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
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

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", title='" + name + '\'' +
                '}';
    }
}