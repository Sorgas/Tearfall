package stonering.objects.local_actors.items.selectors;

import stonering.enums.NumberRelationsEnum;
import stonering.enums.materials.MaterialMap;
import stonering.objects.local_actors.items.Item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Weak descriptor of item used to query any suitable item.
 *
 * @author Alexander on 08.07.2018.
 */
public class ItemSelector2 {
    private ArrayList<String> titles;
    private ArrayList<String> materialTypes;
    private HashMap<String, Integer> requiredProperties;
    private ArrayList<String> requiredAspects;
    private NumberRelationsEnum propertiesBound; //less means that only properties with value less than specified are valid.


    public boolean isSuitableItem(Item item) {
        if (titles != null && !titles.isEmpty()) {
            if (!titles.contains(item.getTitle())) {
                return false;
            }
        }
        ArrayList<String> types = MaterialMap.getInstance().getMaterial(item.getMaterial()).getTypes();
        if (materialTypes != null && !materialTypes.isEmpty()) {
            for (String type : types) {
                if (materialTypes.contains(type)) {
                    return true;
                }
            }
            return false;
        }
        return true;
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

    public ArrayList<String> getMaterialTypes() {
        return materialTypes;
    }

    public void setMaterialTypes(ArrayList<String> materialTypes) {
        this.materialTypes = materialTypes;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }
}
