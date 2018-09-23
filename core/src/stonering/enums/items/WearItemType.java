package stonering.enums.items;

import java.util.ArrayList;

/**
 * Item type aspect for wear.
 *
 * @author Alexander on 11.09.2018.
 */
public class WearItemType {
    private String bodyTemplate;
    private ArrayList<String> allBodyParts;
    private ArrayList<String> requiredBodyParts;

    private float baseInsulation;
    private float baseWetResistance;
    private int layer;

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public ArrayList<String> getAllBodyParts() {
        return allBodyParts;
    }

    public void setAllBodyParts(ArrayList<String> allBodyParts) {
        this.allBodyParts = allBodyParts;
    }

    public float getBaseInsulation() {
        return baseInsulation;
    }

    public void setBaseInsulation(float baseInsulation) {
        this.baseInsulation = baseInsulation;
    }

    public float getBaseWetResistance() {
        return baseWetResistance;
    }

    public void setBaseWetResistance(float baseWetResistance) {
        this.baseWetResistance = baseWetResistance;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public ArrayList<String> getRequiredBodyParts() {
        return requiredBodyParts;
    }

    public void setRequiredBodyParts(ArrayList<String> requiredBodyParts) {
        this.requiredBodyParts = requiredBodyParts;
    }
}
