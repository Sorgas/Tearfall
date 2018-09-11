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
}
