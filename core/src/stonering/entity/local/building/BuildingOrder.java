package stonering.entity.local.building;

import stonering.entity.job.designation.Designation;
import stonering.entity.job.Task;
import stonering.entity.local.item.selectors.ItemSelector;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.HashMap;

/**
 * Order for building. Created in {@link BuildingDesignationSequence}.
 * Contains all information for creating {@link Task}task and {@link Designation}.
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
            Logger.TASKS.logWarn("Trying to add item selector to invalid part name in blueprint " + blueprint.getName());
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

    public HashMap<String, ItemSelector> getItemSelectors() {
        return itemSelectors;
    }
}
