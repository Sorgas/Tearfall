package stonering.util.lang;

/**
 * Wraparound int to store in maps.
 *
 * @author Alexander on 14.11.2019.
 */
public class MutableInteger {
    public int value = 0;

    public MutableInteger() {
    }

    public MutableInteger(int value) {
        this.value = value;
    }
}
