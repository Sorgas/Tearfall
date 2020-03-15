package stonering.util.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static stonering.enums.OrientationEnum.*;

class RotationUtilTest {

    @Test
    void testRotation() {
        assertEquals(RotationUtil.rotate(N, true), E);
        assertEquals(RotationUtil.rotate(N, false), W);
        assertEquals(RotationUtil.rotate(E, true), S);
        assertEquals(RotationUtil.rotate(E, false), N);
        assertEquals(RotationUtil.rotate(S, true), W);
        assertEquals(RotationUtil.rotate(S, false), E);
        assertEquals(RotationUtil.rotate(W, true), N);
        assertEquals(RotationUtil.rotate(W, false), S);
    }

    @Test
    void testVectorRotation() {
        assertEquals(new IntVector2(3, 3), RotationUtil.rotateVector(new IntVector2(3, 3), N));
        assertEquals(new IntVector2(3, -3), RotationUtil.rotateVector(new IntVector2(3, 3), E));
        assertEquals(new IntVector2(-3, -3), RotationUtil.rotateVector(new IntVector2(3, 3), S));
        assertEquals(new IntVector2(-3, 3), RotationUtil.rotateVector(new IntVector2(3, 3), W));

        assertEquals(new IntVector2(4, 2), RotationUtil.rotateVector(new IntVector2(4, 2), N));
        assertEquals(new IntVector2(2, -4), RotationUtil.rotateVector(new IntVector2(4, 2), E));
        assertEquals(new IntVector2(-4, -2), RotationUtil.rotateVector(new IntVector2(4, 2), S));
        assertEquals(new IntVector2(-2, 4), RotationUtil.rotateVector(new IntVector2(4, 2), W));
    }

    @Test
    void testReverseVectorRotation() {
        assertEquals(new IntVector2(3, 3), RotationUtil.unrotateVector(new IntVector2(3, 3), N));
        assertEquals(new IntVector2(-3, 3), RotationUtil.unrotateVector(new IntVector2(3, 3), E));
        assertEquals(new IntVector2(-3, -3), RotationUtil.unrotateVector(new IntVector2(3, 3), S));
        assertEquals(new IntVector2(3, -3), RotationUtil.unrotateVector(new IntVector2(3, 3), W));
        assertEquals(new IntVector2(4, 2), RotationUtil.unrotateVector(new IntVector2(4, 2), N));
        assertEquals(new IntVector2(-2, 4), RotationUtil.unrotateVector(new IntVector2(4, 2), E));
        assertEquals(new IntVector2(-4, -2), RotationUtil.unrotateVector(new IntVector2(4, 2), S));
        assertEquals(new IntVector2(2, -4), RotationUtil.unrotateVector(new IntVector2(4, 2), W));
    }

    @Test
    void testNormalization() {
        assertEquals(new IntVector2(3, 3), RotationUtil.normalizeWithSize(new IntVector2(3, 3), new IntVector2(4, 3)));
        assertEquals(new IntVector2(3, 2), RotationUtil.normalizeWithSize(new IntVector2(-1, -1), new IntVector2(4, 3)));
        assertEquals(new IntVector2(2, 1), RotationUtil.normalizeWithSize(new IntVector2(-2, -2), new IntVector2(4, 3)));
        assertEquals(new IntVector2(0, 0), RotationUtil.normalizeWithSize(new IntVector2(0, 0), new IntVector2(4, 3)));
    }
}