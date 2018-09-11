package stonering.enums.items;

import java.util.ArrayList;

/**
 * Item type aspect for wear.
 *
 * @author Alexander on 11.09.2018.
 */
public class WearItemType {
    private String bodyTemplate;
    private ArrayList<String> bodyParts;

    private float baseInsulation;
    private float baseWetResistance;

    private ArrayList<String> itemTypes; //TODO replace with enum
    private float innerVolume;

    public String getBodyTemplate() {
        return bodyTemplate;
    }

    public void setBodyTemplate(String bodyTemplate) {
        this.bodyTemplate = bodyTemplate;
    }

    public ArrayList<String> getBodyParts() {
        return bodyParts;
    }

    public void setBodyParts(ArrayList<String> bodyParts) {
        this.bodyParts = bodyParts;
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

    public ArrayList<String> getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(ArrayList<String> itemTypes) {
        this.itemTypes = itemTypes;
    }

    public float getInnerVolume() {
        return innerVolume;
    }

    public void setInnerVolume(float innerVolume) {
        this.innerVolume = innerVolume;
    }
}
