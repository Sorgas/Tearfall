package stonering.enums.designations;

import stonering.enums.unit.job.JobsEnum;

import java.util.HashMap;

import static stonering.enums.unit.job.JobsEnum.*;

/**
 * Enum of designation types. All build designation are combined, because logically
 * they are one order and detailed with building name provided on task creation.
 *
 * @author Alexander Kuzyakov
 */
public enum DesignationTypeEnum {
    NONE(0, "none", JobsEnum.NONE),
    DIG(1, "digging wall", MINER), // removes walls and ramps. leaves floor
    STAIRS(2, "cutting stairs", MINER), // cuts stairs from wall.
    RAMP(3, "cutting ramp", MINER), // digs ramp and upper cell.
    CHANNEL(4, "digging channel", MINER), // digs cell and ramp on lower level
    CHOP(5, "chopping trees", LUMBERJACK), // chop trees in th area
    CUT(6, "cutting plants", HARVESTER), // cut plants
    HARVEST(7, "harvesting plants", HARVESTER), // harvest plants
    BUILD(8, "building", BUILDER); // build construction or building

    private static HashMap<Byte, DesignationTypeEnum> map;
    public final byte CODE;
    private String text;
    private String job;

    static {
        map = new HashMap<>();
        for (DesignationTypeEnum type : DesignationTypeEnum.values()) {
            map.put(type.CODE, type);
        }
    }

    DesignationTypeEnum(int code, String text, JobsEnum job) {
        this.CODE = (byte) code;
        this.text = text;
        this.job = job.NAME;
    }

    public String getText() {
        return text;
    }

    public DesignationTypeEnum getType(int code) {
        return map.get(code);
    }
}
