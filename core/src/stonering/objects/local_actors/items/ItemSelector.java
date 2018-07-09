package stonering.objects.local_actors.items;

import stonering.enums.NumberRelationsEnum;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Weak descriptor of item used to query any suitable item.
 * @author Alexander on 08.07.2018.
 */
public class ItemSelector {
    private String title;
    private HashMap<String, Integer> requiredProperties;
    private ArrayList<String> requiredAspects;
    private NumberRelationsEnum propertiesBound; //less means that only properties with value less than specified are valid.


    public boolean isSuitableItem(Item item) {
        if (title != null) {
            if (title.equals(item.getTitle())) {
                return false;
            }
        }
        if (requiredProperties != null) {
            requiredProperties.keySet().forEach(key -> {
                propertiesBound.check(item.get)
            });
        }
    }

    private boolean checkProperties(Item item) {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HashMap<String, Integer> getRequiredProperties() {
        return requiredProperties;
    }

    public void setRequiredProperties(HashMap<String, Integer> requiredProperties) {
        this.requiredProperties = requiredProperties;
    }

    public ArrayList<String> getRequiredAspects() {
        return requiredAspects;
    }

    public void setRequiredAspects(ArrayList<String> requiredAspects) {
        this.requiredAspects = requiredAspects;
    }
}
