package stonering.objects.common;

import stonering.global.utils.Position;

import java.util.ArrayList;

public class Path {
    private ArrayList<Position> path;

    public Path() {
        path = new ArrayList<>();
    }

    public void addPoint(Position position) {
        path.add(0, position);
    }

    public boolean isFinished() {
        return path.isEmpty();
    }

    public Position getNextPosition() {
        return path.get(0);
    }

    public Position pollNextPosition() {
        if (!path.isEmpty())
            return path.remove(0);
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        path.forEach((position) -> {
            builder.append(position.toString());
            builder.append('\n');
        });
        return builder.toString();
    }
}
