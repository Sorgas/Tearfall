package stonering.enums.materials;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by Alexander on 04.06.2017.
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
    private String description;

    private Color color;
    private byte atlasY;
    private float density;
    private int hardness;
    private int melting_point;

    public Material() {
    }

    public Material(String name, Color color, byte atlasY, int id) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.atlasY = atlasY;
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

    public byte getAtlasY() {
        return atlasY;
    }

    public void setAtlasY(byte atlasY) {
        this.atlasY = atlasY;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMelting_point() {
        return melting_point;
    }

    public void setMelting_point(int melting_point) {
        this.melting_point = melting_point;
    }

    @Override
    public String toString() {
        return "Material{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}