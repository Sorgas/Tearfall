package stonering.generators.localgen.generators;

import stonering.game.model.local_map.LocalMap;
import stonering.generators.PerlinNoiseGenerator;
import stonering.generators.localgen.LocalGenConfig;
import stonering.generators.localgen.LocalGenContainer;

/**
 * Detarmines local climate conditions and stores them in {@link stonering.generators.localgen.LocalGenContainer}
 *
 * @author Alexander Kuzyakov on 28.02.2019.
 */
public class LocalClimateGenerator extends LocalGenerator {
    private LocalGenContainer container;
    private LocalGenConfig config;
    private LocalMap localMap;
    private PerlinNoiseGenerator noiseGenerator;
    private float maxTemp;
    private float minTemp;
    private float rainfall;
    private int areaSize;

    public LocalClimateGenerator(LocalGenContainer container) {
        super(container);
    }

    public void execute() {
        //TODO
    }
}
