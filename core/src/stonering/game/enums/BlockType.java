package stonering.game.enums;

/**
 * Created by Alexander on 10.06.2017.
 */
public enum BlockType {
    SPACE((byte) 0) {
    },
    WALL((byte) 1) {

    },
    CEIL((byte) 2){

    },
    RAMP((byte) 3){

    },
    STAIRS((byte) 4){

    };

    byte code;
    BlockType(byte code) {
        this.code = code;
    }

    public byte getCode() {
        return code;
    }
}
