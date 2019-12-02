package stonering.entity.building;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.Task;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.game.controller.controllers.designation.BuildingDesignationSequence;
import stonering.util.geometry.Position;

import java.util.HashMap;

/**
 * Order for building. Created in {@link BuildingDesignationSequence}.
 * Contains all information for creating {@link Task}task and {@link Designation}.
 * TODO introduce ingredient order.
 *
 * @author Alexander on 06.03.2019.
 */
public class BuildingOrder {
    public final Blueprint blueprint;
    public Position position;
    public final HashMap<String, IngredientOrder> parts; // building parts to their ingredients

    public BuildingOrder(Blueprint blueprint, Position position) {
        this.blueprint = blueprint;
        this.position = position;
        parts = new HashMap<>();
    }
}
