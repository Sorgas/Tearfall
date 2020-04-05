package stonering.util.geometry;

import com.sun.istack.NotNull;
import stonering.enums.OrientationEnum;

import static stonering.enums.OrientationEnum.*;

/**
 * Contains game-specific rotation operations.
 *
 * @author Alexander on 11.03.2020.
 */
public class RotationUtil {

    /**
     * Flips size values for E and W orientations.
     */
    public static IntVector2 orientSize(IntVector2 size, OrientationEnum orientation) {
        if (orientation == E || orientation == W) {
            return new IntVector2(size.y, size.x);
        } else {
            return new IntVector2(size.x, size.y);
        }
    }

    public static OrientationEnum rotateOrientation(@NotNull OrientationEnum orientation, boolean clockwise) {
        switch (orientation) {
            case N:
                orientation = clockwise ? E : W;
                break;
            case E:
                orientation = clockwise ? S : N;
                break;
            case S:
                orientation = clockwise ? W : E;
                break;
            case W:
                orientation = clockwise ? N : S;
                break;
        }
        return orientation;
    }

    /**
     * Rotates given vector from N orientation to given one.
     */
    public static IntVector2 rotateVector(IntVector2 vector, OrientationEnum to) {
        IntVector2 newVector = new IntVector2(vector);
        switch (to) {
            case S: // 180 rotation
                newVector.x = -vector.x;
                newVector.y = -vector.y;
                break;
            case E: // cw 90 rotation
                newVector.x = vector.y;
                newVector.y = -vector.x;
                break;
            case W: // ccw 90 rotation
                newVector.x = -vector.y;
                newVector.y = vector.x;
                break;
        }
        return newVector;
    }

    /**
     * Rotates vector to N orientation from given one.
     */
    public static IntVector2 unrotateVector(IntVector2 vector, OrientationEnum from) {
        return unrotateVector(vector.x, vector.y, from);
    }

    public static IntVector2 unrotateVector(int x, int y, OrientationEnum from) {
        IntVector2 newVector = new IntVector2(x, y);
        switch (from) {
            case S: // 180 rotation
                newVector.x = -x;
                newVector.y = -y;
                break;
            case E: // ccw 90 rotation
                newVector.x = -y;
                newVector.y = x;
                break;
            case W: // cw 90 rotation
                newVector.x = y;
                newVector.y = -x;
                break;
        }
        return newVector;
    }
    
    /**
     * Normalizes vector to have all components positive and still point to same point in a rectangle of given size.
     */
    public static IntVector2 normalizeWithSize(IntVector2 vector, IntVector2 size) {
        while(vector.x < 0) vector.x += size.x; // TODO replace with formula
        while(vector.y < 0) vector.y += size.y;
        return vector;
    }
}
