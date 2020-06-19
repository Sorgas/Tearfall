package stonering.util.geometry;

import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander on 19.06.2020.
 */
public class PositionUtil {
    public static List<Position> neighbourDeltas = Arrays.asList(
            new Position(1, 0, 0),
            new Position(0, 1, 0),
            new Position(-1, 0, 0),
            new Position(0, -1, 0)
    );

    public static List<Position> allNeighbourDeltas = Arrays.asList(
            new Position(0, 1, 0),
            new Position(0, -1, 0),
            new Position(1, 0, 0),
            new Position(1, 1, 0),
            new Position(1, -1, 0),
            new Position(-1, 0, 0),
            new Position(-1, 1, 0),
            new Position(-1, -1, 0)
    );

    public static List<Position> lowerAndSameNeighbourDeltas = Arrays.asList(
            new Position(0, 1, 0),
            new Position(0, -1, 0),
            new Position(1, 0, 0),
            new Position(1, 1, 0),
            new Position(1, -1, 0),
            new Position(-1, 0, 0),
            new Position(-1, 1, 0),
            new Position(-1, -1, 0),
            new Position(0, 0, -1),
            new Position(0, 0, 1)
    );
}
