package stonering.game.enums;

import java.util.HashMap;

/**
 * Created by Alexander on 10.06.2017.
 */
public enum BlockTypesEnum {
    SPACE((byte) 0,-1) {
    },
    WALL((byte) 1,0) {

    },
    CEIL((byte) 2,1){

    },
    RAMP((byte) 3,2){

    },
    STAIRS((byte) 4,3){

    };

    private byte code;
    private int atlasX;
    private static HashMap<Byte, BlockTypesEnum> map;

    static {
        for (BlockTypesEnum type: BlockTypesEnum.values()) {
            map.put(type.code, type);
        }
    }

    BlockTypesEnum(byte code, int atlasX) {
        this.code = code;
        this.atlasX = atlasX;
    }

    public byte getCode() {
        return code;
    }

    public int getAtlasX() {
        return atlasX;
    }

    public static BlockTypesEnum getType(byte code) {
        return map.get(code);
    }
}
