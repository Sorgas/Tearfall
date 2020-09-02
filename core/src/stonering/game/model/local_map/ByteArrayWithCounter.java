package stonering.game.model.local_map;

import stonering.util.UtilByteArray;
import stonering.util.lang.MutableInteger;

import java.util.HashMap;
import java.util.Map;

/**
 * Byte array that keeps quantities of stored values in a map.
 *
 * @author Alexander on 14.11.2019.
 */
public class ByteArrayWithCounter extends UtilByteArray {
    public Map<Byte, MutableInteger> numbers; // counts number of cells in areas

    public ByteArrayWithCounter(int xSize, int ySize, int zSize) {
        super(xSize, ySize, zSize);
        numbers = new HashMap<>();
        numbers.put((byte) 0, new MutableInteger(xSize * ySize * zSize));
    }

    @Override
    public void set(int x, int y, int z, int value) {
        byte oldValue = get(x, y, z);
        super.set(x, y, z, value);
        updateMap(x, y, z, oldValue);
    }

    @Override
    public void change(int x, int y, int z, byte delta) {
        byte oldValue = get(x, y, z);
        super.change(x, y, z, delta);
        updateMap(x, y, z, oldValue);
    }

    private void updateMap(int x, int y, int z, byte oldValue) {
        byte newValue = get(x, y, z);
        numbers.putIfAbsent(newValue, new MutableInteger()); // increase counter for new value
        numbers.get(newValue).value++;
        MutableInteger old = numbers.get(oldValue);
        if (old.value < 2) {
            numbers.remove(oldValue);
        } else {
            old.value--; // decrease counter for old value
        }
    }
}
