package stonering.global.utils;

/**
 * @author Alexander Kuzyakov on 18.02.2017.
 *
 * Utility class for a line.
 */
public class Line {
    private Position pos1;
    private Position pos2;
    private Position siteA;
    private Position SiteB;
    private float k;
    private float b;

    public Line(Position pos1, Position pos2) {
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public Line() {
        pos1 = new Position(0, 0, 0);
        pos2 = new Position(0, 0, 0);
    }

    private void updateK() {
        k = (pos1.getY() - pos2.getY()) / (pos1.getX() - pos2.getX());
    }

    private void updateB() {
        updateK();
        b = pos1.getY() - k * pos1.getX();
    }

    private Position getMiddle() {
        return new Position(Math.round((pos1.getX() + pos2.getX()) / 2), Math.round((pos1.getY() + pos2.getY()) / 2), 0);

    }

    public Position getPos1() {
        return pos1;
    }

    public void setPos1(Position pos1) {
        this.pos1 = pos1;
    }

    public Position getPos2() {
        return pos2;
    }

    public void setPos2(Position pos2) {
        this.pos2 = pos2;
    }

    public Position getSiteA() {
        return siteA;
    }

    public void setSiteA(Position siteA) {
        this.siteA = siteA;
    }

    public Position getSiteB() {
        return SiteB;
    }

    public void setSiteB(Position siteB) {
        SiteB = siteB;
    }
}