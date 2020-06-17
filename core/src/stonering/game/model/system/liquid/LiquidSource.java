package stonering.game.model.system.liquid;

import stonering.util.geometry.Position;

/**
 * @author Alexander on 17.06.2020.
 */
public class LiquidSource {
    public Position position;
    public int liquid;
    public int intensity;

    public LiquidSource(Position position, int liquid, int intensity) {
        this.position = position;
        this.liquid = liquid;
        this.intensity = intensity;
    }
}
