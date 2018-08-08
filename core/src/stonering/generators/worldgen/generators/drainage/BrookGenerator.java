package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.generators.worldgen.generators.AbstractGenerator;

/**
 * Generates brooks
 *
 * @author Alexander Kuzyakov on 18.01.2018.
 */
public class BrookGenerator extends AbstractGenerator {
    private WorldMap map;
    private int width;
    private int height;
    private int[][] points;

    public BrookGenerator(WorldGenContainer container) {
        super(container);
    }

    private void extractContainer(WorldGenContainer container) {
        width = container.getConfig().getWidth();
        height = container.getConfig().getHeight();
    }

    public boolean execute() {
        points = new int[width][height];
        return true;
    }

    private void countSlopeAngles() {

    }
}
