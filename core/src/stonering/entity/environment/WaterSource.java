package stonering.entity.environment;

import stonering.util.geometry.Position;

/**
 * @author Alexander Kuzyakov on 22.08.2018.
 */
public class WaterSource {
    private Position position;
    private int liquid;

    public WaterSource(Position position, int liquid) {
        this.position = position;
        this.liquid = liquid;
    }
}
