package test;

import stonering.global.utils.Vector;

/**
 * Created by Alexander on 13.03.2018.
 */
public class VectorTest {
    public static void main(String[] arg) {
        for (int x = -2; x < 3; x += 2) {
            for (int y = -2; y < 3; y += 2) {
                Vector vector = new Vector(0, 0, 2, 2);
                Vector vector2 = new Vector(0, 0, x, y);
                System.out.println(x + " " + y);
                System.out.println(vector.sum(vector2).toString());
            }
        }
    }
}
