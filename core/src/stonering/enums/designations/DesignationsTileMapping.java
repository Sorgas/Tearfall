package stonering.enums.designations;

import java.util.HashMap;

/**
 * Mapping of designation demarcation to sprites positions in atlas.
 * <p>
 * @author Alexander Kuzyakov on 27.12.2017.
 */
public enum DesignationsTileMapping {
    DIG(1, 0),
    STAIRS(2, 1),
    RAMP(3, 2),
    CHANNEL(4, 3),
    CHOP(5, 4),
    CUT(6, 5);

    private int code;
    private int atlasX;
    private static HashMap<Integer, Integer> map;

    static {
        map = new HashMap<>();
        for (DesignationsTileMapping type : DesignationsTileMapping.values()) {
            map.put(type.code, type.atlasX);
        }
    }

    DesignationsTileMapping(int code, int atlasX) {
        this.code = code;
        this.atlasX = atlasX;
    }

    public int getCode() {
        return code;
    }

    public int getAtlasX() {
        return atlasX;
    }

    public static int getAtlasX(int code) {
        return map.get(code);
    }
}
