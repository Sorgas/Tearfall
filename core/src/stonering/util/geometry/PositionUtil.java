package stonering.util.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Alexander on 19.06.2020.
 */
public class PositionUtil {
    public static List<Position> fourNeighbour;

    // 8 on same level except center
    public static List<Position> allNeighbour;

    public static List<Position> waterflow;

    public static List<Position> all = new ArrayList<>();

    static {
        fourNeighbour = Arrays.asList( // four orthogonally adjacent
                new Position(1, 0, 0),
                new Position(0, 1, 0),
                new Position(-1, 0, 0),
                new Position(0, -1, 0)
        );

        allNeighbour = new ArrayList<>(fourNeighbour);
        // four diagonally adjacent
        allNeighbour.add(new Position(1, 1, 0)); 
        allNeighbour.add(new Position(1, -1, 0));
        allNeighbour.add(new Position(-1, 1, 0));
        allNeighbour.add(new Position(-1, -1, 0));

        waterflow = new ArrayList<>(allNeighbour);
        // upper and lower
        waterflow.add(new Position(0, 0, -1));
        waterflow.add(new Position(0, 0, 1));

        for (int z = -1; z <= 1; z++) {
            for (int y = -1; y <= 1; y++) {
                for (int x = -1; x <= 1; x++) {
                    if (x != 0 || y != 0 || z != 0) all.add(new Position(x, y, z));
                }
            }
        }
    }
}
