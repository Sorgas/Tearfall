package stonering.enums.items.type;

import java.util.ArrayList;

/**
 * Item type aspect for wear.
 *
 * @author Alexander on 11.09.2018.
 */
public class WearItemType {
    public static String RIGHT = "right";
    public static String LEFT = "left";
    public static String BOTH = "both";
    private String bodyTemplate;                   // creatures with this template can use item
    private ArrayList<String> allBodyParts;        // body parts, covered by item
    private ArrayList<String> requiredBodyParts;   // body parts, required to use item
    private String side;

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

    public ArrayList<String> getRequiredBodyParts() {
        return requiredBodyParts;
    }

    public void setRequiredBodyParts(ArrayList<String> requiredBodyParts) {
        this.requiredBodyParts = requiredBodyParts;
    }
}
