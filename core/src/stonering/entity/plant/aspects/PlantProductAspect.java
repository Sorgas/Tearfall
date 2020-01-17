package stonering.entity.plant.aspects;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Aspect for holding plant products before harvesting.
 *
 * @author Alexander on 17.01.2020.
 */
public class PlantProductAspect extends Aspect {
    public List<Item> products;


    public PlantProductAspect(Entity entity) {
        super(entity);
        products = new ArrayList<>();
    }
}
