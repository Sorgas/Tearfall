package stonering.enums.items.type;

import java.util.ArrayList;

/**
 * Item type aspect for containers.
 * Determines item which could store it.
 *
 * @author Alexander Kuzyakov on 11.09.2018.
 */
public class ContainerItemType {
    private ArrayList<String> itemTypes; //TODO replace with enum
    private float innerVolume;

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
