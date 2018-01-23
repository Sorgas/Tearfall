package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;

/**
 * Created by Alexander on 18.01.2018.
 */
public class BrookGenerator extends AbstractGenerator {
    private WorldMap map;
    private int width;
    private int height;

    public BrookGenerator(WorldGenContainer container) {
        super(container);
    }

    private void extractContainer(WorldGenContainer container) {
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
    }

    public boolean execute() {
        return true;
    }

    private void countSlopeAngles() {

    }
}
