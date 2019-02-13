package stonering.entity.local.plants.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;

/**
 * Switches plant life stages. Restructures tree if needed to represent growth.
 *
 * @author Alexander on 13.02.2019.
 */
public class TreeGrowthAspect extends Aspect {
    public static final String NAME = "tree_growth";

    public TreeGrowthAspect(AspectHolder aspectHolder) {
        super(aspectHolder);
    }

    /**
     *
     */
    @Override
    public void turn() {

    }

    @Override
    public String getName() {
        return NAME;
    }
}
