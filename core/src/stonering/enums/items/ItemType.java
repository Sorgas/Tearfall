package stonering.enums.items;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Descriptior class of item. Stores all properties, valid to the whole type of items, not for specific ones.
 * (e.g. not material, condition, ownership)
 */
public class ItemType {
    private String title;
    private HashMap<String, Integer> properties;
    private ArrayList<String> aspects;
    private float basicValue;
    private int atlasX;
    private int atlasY;
    private Color color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getAspects() {
        return aspects;
    }

    public void setAspects(ArrayList<String> aspects) {
        this.aspects = aspects;
    }

    public int getAtlasX() {
        return atlasX;
    }

    public void setAtlasX(int atlasX) {
        this.atlasX = atlasX;
    }

    public int getAtlasY() {
        return atlasY;
    }

    public void setAtlasY(int atlasY) {
        this.atlasY = atlasY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getBasicValue() {
        return basicValue;
    }

    public void setBasicValue(float basicValue) {
        this.basicValue = basicValue;
    }

    public HashMap<String, Integer> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, Integer> properties) {
        this.properties = properties;
    }
}
