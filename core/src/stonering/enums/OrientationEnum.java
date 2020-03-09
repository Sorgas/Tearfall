package stonering.enums;

import org.apache.commons.lang3.ArrayUtils;
import stonering.util.global.Logger;

public enum OrientationEnum {
    N("n"),
    S("s"),
    E("e"),
    W("w");

    public final String SPRITE_SUFFIX; // used to select sprite 

    OrientationEnum(String suffix) {
        SPRITE_SUFFIX = suffix;
    }

    public int[] defineSize(int[] nSize) {
        int[] newSize = nSize.clone();
        if(this == E || this == W) ArrayUtils.reverse(newSize);
        return newSize;
    }

    /**
     * Returns coordinate of this orientation for given North orientation;
     */
    public int[] rotate(int[] nPoint) {
        int[] newPoint = nPoint.clone();
        switch(this) {
            case N:
                return newPoint;
            case S: // 180 rotation
                newPoint[0] = - nPoint[0];
                newPoint[1] = - nPoint[1];
                return newPoint;
            case E: // -90 rotation
                newPoint[0] = nPoint[1];
                newPoint[1] = - nPoint[0];
                return newPoint;
            case W: // 90 rotation
                newPoint[0] = - nPoint[1];
                newPoint[1] = nPoint[0];
                return newPoint;
        }
        return newPoint;
    }

    /**
     * Gives coordinate in N orientation for given coordinate in this orientation;
     */
    public int[] unrotate(int x, int y) {
        switch(this) {
            case N:
                return new int[] {x, y};
            case S: // 180 rotation
                return new int[] {-x, -y};
            case E: // -90 rotation
                return new int[] {y, -x};
            case W: // 90 rotation
                return new int[] {-y, x};
            default:
                Logger.BUILDING.logError("qweqweqwe");
                return new int[] {x, y};
        }
    }
}
