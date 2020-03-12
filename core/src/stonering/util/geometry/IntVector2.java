package stonering.util.geometry;

/**
 * @author Alexander on 11.03.2020.
 */
public class IntVector2 {
    public int x, y;

    public IntVector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public IntVector2() {
        this(0, 0);
    }
    
    public IntVector2(IntVector2 source) {
        this(source.x, source.y);
    }
    
    public IntVector2(int[] source) {
        this(source[0], source[1]);
    }
}
