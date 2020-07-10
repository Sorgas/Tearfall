package stonering.generators.worldgen.generators.elevation;

import stonering.generators.worldgen.WorldGenConfig;
import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.WorldGenerator;

public class FractalPerlinElevationGenerator extends WorldGenerator {
    private WorldGenConfig config;
    private float[][] elevatio;


    public FractalPerlinElevationGenerator(WorldGenContainer container) {
        super(container);
        extractContainer(container);
    }

    @Override
    public boolean execute() {
        return false;
    }

    private void extractContainer(WorldGenContainer container) {
        config = container.config;
    }

}
