package stonering.enums.unit.job;

import java.util.HashSet;
import java.util.Set;

/**
 * Holds all possible jobs.
 * Some {@link stonering.entity.job.Task}s require related job to be enabled.
 * They can be enabled for units with {@link stonering.entity.unit.aspects.JobsAspect}.
 *
 *
 * @author Alexander on 08.08.2019.
 */
public enum JobsEnum {
    NONE("none"),
    MINER("miner"),
    LUMBERJACK("lumberjack"),
    HARVESTER("harvester"),
    BUILDER("builder"),
    ;

    private static final Set<String> jobs = new HashSet<>();

    static {
        for (JobsEnum value : JobsEnum.values()) {
            jobs.add(value.NAME);
        }
    }

    public final String NAME;

    JobsEnum(String name) {
        this.NAME = name;
    }
}
