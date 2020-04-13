package stonering.entity.building;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.Task;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.util.geometry.Position;

import java.util.HashMap;

/**
 * Order for building. Created in {@link BuildingMaterialSelectMenu}. 
 * Contains all information for creating {@link Task}task and {@link Designation}.
 * {@link IngredientOrder}s are created for all building parts, defined in {@link Blueprint}.
 * TODO introduce ingredient order.
 *
 * @author Alexander on 06.03.2019.
 */
public class BuildingOrder {
    public final Blueprint blueprint;
    public Position position;
    public OrientationEnum orientation;
    public final HashMap<String, IngredientOrder> parts; // building parts to their ingredients

    public BuildingOrder(Blueprint blueprint, Position position) {
        this.blueprint = blueprint;
        this.position = position;
        parts = new HashMap<>();
        blueprint.parts.forEach((partName, ingredient) -> parts.put(partName, new IngredientOrder(ingredient)));
    }

}
