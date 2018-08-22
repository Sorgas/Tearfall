package stonering.objects.local_actors.environment;

import stonering.global.utils.Position;

/**
 * @author Alexander on 22.08.2018.
 */
public class WaterSource {
    private Position position;
    private int liquid;

    public WaterSource(Position position, int liquid) {
        this.position = position;
        this.liquid = liquid;
    }
}
