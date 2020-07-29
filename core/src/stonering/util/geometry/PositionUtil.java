package stonering.util.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander on 19.06.2020.
 */
public class PositionUtil {
    public static List<Position> fourNeighbourDeltas = Arrays.asList(
            new Position(1, 0, 0),
            new Position(0, 1, 0),
            new Position(-1, 0, 0),
            new Position(0, -1, 0)
    );

    // 8 on same level except center
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

    public static List<Position> upperNeighbourDeltas = Arrays.asList(
            new Position(0, 1, 1),
            new Position(0, -1, 1),
            new Position(1, 0, 1),
            new Position(1, 1, 1),
            new Position(1, -1, 1),
            new Position(-1, 0, 1),
            new Position(-1, 1, 1),
            new Position(-1, -1, 1)
    );

    public static List<Position> all = new ArrayList<>();

    static {
        for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    if (x != 0 || y != 0 || z != 0) all.add(new Position(x, y, z));
                }
            }
        }
    }
}
