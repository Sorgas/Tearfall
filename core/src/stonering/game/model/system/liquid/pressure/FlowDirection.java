package stonering.game.model.system.liquid.pressure;

/**
 * @author Alexander_Kuzyakov on 03.08.2020.
 */
public enum FlowDirection {
    TOP(0),
    RIGHT(1),
    BOTTOM(2),
    LEFT(3);

    public final int id;

    FlowDirection(int id){
        this.id = id;
    }
}
