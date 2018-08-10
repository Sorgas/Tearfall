package stonering.generators.localgen.generators;

import com.badlogic.gdx.math.Bezier;
import com.badlogic.gdx.math.Vector2;
import javafx.geometry.Pos;
import stonering.game.core.model.LocalMap;
import stonering.enums.blocks.BlockTypesEnum;
import stonering.generators.localgen.LocalGenContainer;
import stonering.generators.worldgen.WorldMap;
import stonering.global.utils.Position;

import java.util.ArrayList;

/**
 * Generates rivers and brooks on local worldMap.
 * All incoming flows merge into flow on local map.
 *
 * @author Alexander Kuzyakov on 10.07.2017.
 */
public class LocalRiverGenerator {
    private WorldMap worldMap;
    private LocalMap localMap;
    private Position location;
    private ArrayList<Position> incomingRivers; // absolute coords on world map
    private ArrayList<Position> incomingBrooks; // absolute coords on world map
    private Vector2 currentFlow;
    private boolean currentFlowIsRiver;
    private Position mainInflow; // absolute coords on world map

    public LocalRiverGenerator(LocalGenContainer container) {
        worldMap = container.getWorldMap();
        localMap = container.getLocalMap();
        location = container.getConfig().getLocation();
    }

    public void execute() {
        lookupFlows();
        determineMailInflow();
    }

    /**
     * Carves bed of current flow and its main inflow.
     */
    private void makeMainFlow() {
        Position startPos = getPositionOnBorderByWorldCoords(mainInflow);
        Vector2 start = new Vector2(startPos.getX(), startPos.getY());
        Vector2 end = currentFlow.cpy().nor();
        Position endPos = getPositionOnBorderByWorldCoords(new Position(Math.round(end.x), Math.round(end.y), 0));
        end = new Vector2(endPos.getX(), endPos.getY());

        Vector2[] vectors = new Vector2[4];
        Bezier<Vector2> bezier = new Bezier<>();
    }

    private void placeRiver(int dx, int dy) {
        System.out.println("placing river");
        int x = localMap.getxSize() / 2;
        int y = localMap.getySize() / 2;
        while (localMap.inMap(x, y, 0)) {
            localMap.setBlock(x, y, 0, BlockTypesEnum.SPACE, 1);
            x += dx;
            y += dy;
        }
    }

    private Position getPositionOnBorderByWorldCoords(Position position) {
        int dx = location.getX() - position.getX();
        int dy = location.getY() - position.getY();
        int x = 0;
        int y = 0;
        if (dx != 0) {
            x = dx > 0 ? 1 : -1;
        }
        if (dy != 0) {
            y = dy > 0 ? 1 : -1;
        }
        return new Position(x, y, 0);
    }

    /**
     * Fills collections of inflows and sets current flow.
     */
    private void lookupFlows() {
        for (int dx = -1; dx < 2; dx++) {
            for (int dy = -1; dy < 2; dy++) {
                int x = location.getX() + dx;
                int y = location.getY() + dy;
                if ((dx != 0 || dy != 0) && worldMap.inMap(dx, dy)) {
                    if (worldMap.getRiver(x, y) != null && isInflow(dx, dy, worldMap.getRiver(x, y).cpy())) {
                        incomingRivers.add(new Position(x, y, 0));
                    }
                    if (worldMap.getBrook(x, y) != null && isInflow(dx, dy, worldMap.getBrook(x, y).cpy())) {
                        incomingBrooks.add(new Position(x, y, 0));
                    }
                }
            }
        }
        currentFlow = worldMap.getRiver(location.getX(), location.getY());
        if (currentFlow != null) {
            currentFlowIsRiver = true;
        } else {
            currentFlow = worldMap.getBrook(location.getX(), location.getY());
            currentFlowIsRiver = currentFlow == null;
        }
    }

    /**
     * Checks if given vector points to current position.
     *
     * @param relativeX
     * @param relativeY
     * @param vector
     * @return
     */
    private boolean isInflow(int relativeX, int relativeY, Vector2 vector) {
        vector.nor();
        return Math.round(vector.x) + relativeX == 0 && Math.round(vector.y) + relativeY == 0;
    }

    private void determineMailInflow() {
        if (currentFlowIsRiver || !incomingRivers.isEmpty()) {
            // if current flow is river, only river inflow can be main.
            // if current flow is brook and river inflows present, one of them should be main as well.
            incomingRivers.forEach(position -> {
                if (mainInflow == null || // first found inflow will go
                        worldMap.getRiver(position.getX(), position.getY()).len() > worldMap.getRiver(mainInflow.getX(), mainInflow.getY()).len()) { // choose inflow with greater amount of water
                    mainInflow = position;
                }
            });
        } else { // no rivers, look for biggest brook
            incomingBrooks.forEach(position -> {
                if (mainInflow == null || // first found inflow will go
                        worldMap.getBrook(position.getX(), position.getY()).len() > worldMap.getBrook(mainInflow.getX(), mainInflow.getY()).len()) { // choose inflow with greater amount of water
                    mainInflow = position;
                }
            });
        }
    }
}