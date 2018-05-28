package stonering.enums.items;

import com.badlogic.gdx.graphics.Color;
import stonering.objects.local_actors.items.aspects.PropertyAspect;

import java.util.ArrayList;

/**
 * Descriptior class of item. Stores all properties, valid to the whole type of items, not for specific ones.
 * (e.g. not material, condition, ownership)
 */
public class ItemType {
    private String title;
    private ArrayList<PropertyAspect> aspects;
    private ArrayList<String> reactions;
    private ArrayList<String> reactionArgs;
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

    public ArrayList<PropertyAspect> getAspects() {
        return aspects;
    }

    public void setAspects(ArrayList<PropertyAspect> aspects) {
        this.aspects = aspects;
    }

    public ArrayList<String> getReactions() {
        return reactions;
    }

    public void setReactions(ArrayList<String> reactions) {
        this.reactions = reactions;
    }

    public ArrayList<String> getReactionArgs() {
        return reactionArgs;
    }

    public void setReactionArgs(ArrayList<String> reactionArgs) {
        this.reactionArgs = reactionArgs;
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
}
