package stonering.entity.building;

import stonering.entity.crafting.IngredientOrder;
import stonering.entity.crafting.ItemConsumingOrder;
import stonering.entity.job.designation.Designation;
import stonering.entity.job.Task;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.blueprint.Blueprint;
import stonering.stage.building.BuildingMaterialSelectMenu;
import stonering.util.geometry.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Order for building. Created in {@link BuildingMaterialSelectMenu}. 
 * Contains all information for creating {@link Task}task and {@link Designation}.
 * {@link IngredientOrder}s are created for all building parts, defined in {@link Blueprint}.
 *
 * @author Alexander on 06.03.2019.
 */
public class BuildingOrder extends ItemConsumingOrder {
    public final Blueprint blueprint;
    public Position position;
    public OrientationEnum orientation;

    public BuildingOrder(Blueprint blueprint, Position position) {
        super();
        this.blueprint = blueprint;
        this.position = position;
        // TODO move to superclass
        blueprint.ingredients.forEach((key, ingredientList) ->
                ingredientOrders.put(key, ingredientList.stream().map(IngredientOrder::new).collect(Collectors.toList())));
    }
}
