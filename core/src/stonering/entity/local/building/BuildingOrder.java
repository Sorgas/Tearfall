package stonering.entity.local.building;

import stonering.entity.local.items.selectors.ItemSelector;
import stonering.util.geometry.Position;

import java.util.HashMap;

/**
 *
 *
 * @author Alexander on 06.03.2019.
 */
public class BuildingOrder {
    private Blueprint blueprint;
    private Position position;
    private HashMap<String, ItemSelector> itemSelectors; // building part name to selected materials

    public BuildingOrder(Blueprint blueprint, Position position) {
        this.blueprint = blueprint;
        this.position = position;
        itemSelectors = new HashMap<>();
    }

    public void addItemSelectorForPart(String partName, ItemSelector selector) {
        if(blueprint.getStepByPartName(partName) == null) {
            //TODO  log warn
            return;
        }
        itemSelectors.put(partName, selector);
    }

    public Blueprint getBlueprint() {
        return blueprint;
    }

    public void setBlueprint(Blueprint blueprint) {
        this.blueprint = blueprint;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
