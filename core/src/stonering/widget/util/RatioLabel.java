package stonering.widget.util;

/**
 * Label that shows some ratio (two integers over slash).
 *
 * @author Alexander on 20.12.2019.
 */
public class RatioLabel extends ValueFormatLabel {
    private static final String RATIO_FORMAT = "%d/%d";

    public RatioLabel(int firstValue, int secondValue) {
        super(RATIO_FORMAT, new Object[]{firstValue, secondValue});
    }

    public int getDividend() {
        return (int) getValue(0);
    }

    public void setDividend(int dividend) {
        setValue(0, dividend);
        updateText();
    }

    public int getDivisor() {
        return (int) getValue(1);
    }

    public void setDivisor(int divisor) {
        setValue(1, divisor);
        updateText();
    }
}
