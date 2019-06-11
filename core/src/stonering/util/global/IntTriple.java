package stonering.util.global;

/**
 * Stores 3 int values.
 * @author Alexander_Kuzyakov on 11.06.2019.
 */
public class IntTriple {
    private int val1;
    private int val2;
    private int val3;

    public IntTriple() {
        this(0, 0, 0);
    }

    public IntTriple(int val1, int val2, int val3) {
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public int getVal1() {
        return val1;
    }

    public void setVal1(int val1) {
        this.val1 = val1;
    }

    public int getVal2() {
        return val2;
    }

    public void setVal2(int val2) {
        this.val2 = val2;
    }

    public int getVal3() {
        return val3;
    }

    public void setVal3(int val3) {
        this.val3 = val3;
    }
}
