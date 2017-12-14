package stonering.objects.common;

import stonering.global.utils.Position;

import java.util.ArrayList;

public class Path {
    private ArrayList<Position> path;

    public Path() {
        path = new ArrayList<>();
    }

    public void addPoint(Position position) {
        path.add(position);
    }

    public boolean isFinished() {
        return path.isEmpty();
    }

    public Position getNextPosition() {
        return path.get(0);
    }

    public Position pollNextPosition() {
        return path.remove(0);
    }
}
