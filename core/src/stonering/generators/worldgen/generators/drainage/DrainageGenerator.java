package stonering.generators.worldgen.generators.drainage;

import stonering.generators.worldgen.WorldGenContainer;
import stonering.generators.worldgen.generators.AbstractGenerator;
import stonering.global.utils.Position;

/**
 * @author Alexander Kuzyakov
 */
public class DrainageGenerator extends AbstractGenerator {


    public DrainageGenerator(WorldGenContainer container) {
        super(container);
    }

    @Override
    public boolean execute() {
        extractContainer();
        return false;
    }

    private void extractContainer() {

    }

    private void createSlopesMap() {

    }

    private void createEvaporationMap() {

    }

    private int countSlope(Position pos) {
        return 0;
    }
}
