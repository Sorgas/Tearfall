package stonering.util.geometry;

/**
 *
 *
 * @author Alexander on 04.03.2019.
 */
public class Area {
    private Position position1;
    private Position position2;

    public Area(Position position1, Position position2) {
        if(position1 == null || position2 == null) throw new IllegalArgumentException("Cannot create area with null positions.");
        this.position1 = position1;
        this.position2 = position2;
    }

    public Position getPosition1() {
        return position1;
    }

    public void setPosition1(Position position1) {
        this.position1 = position1;
    }

    public Position getPosition2() {
        return position2;
    }

    public void setPosition2(Position position2) {
        this.position2 = position2;
    }
}
