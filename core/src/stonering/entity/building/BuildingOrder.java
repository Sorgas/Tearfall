package stonering.entity.building;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.Task;
import stonering.entity.item.selectors.ItemSelector;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.util.geometry.Position;
import stonering.util.global.Logger;

import java.util.HashMap;

/**
 * Order for building. Created in {@link BuildingDesignationSequence}.
 * Contains all information for creating {@link Task}task and {@link Designation}.
 * TODO introduce ingredient order.
 *
 * @author Alexander on 06.03.2019.
 */
public class BuildingOrder {
    public Blueprint blueprint;
    private Position position;
    private HashMap<String, ItemSelector> itemSelectors; // building part name to selector
    public final HashMap<String, IngredientOrder> parts; //item parts to their ingredients

    public BuildingOrder(Blueprint blueprint, Position position) {
        this.blueprint = blueprint;
        this.position = position;
        itemSelectors = new HashMap<>();
    }

    public void addItemSelectorForPart(String partName, ItemSelector selector) {
        if(blueprint.mappedComponents.get(partName) == null) {
            Logger.TASKS.logWarn("Trying to add item selector to invalid part name in blueprint " + blueprint.name);
            return;
        }
        itemSelectors.put(partName, selector);
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
