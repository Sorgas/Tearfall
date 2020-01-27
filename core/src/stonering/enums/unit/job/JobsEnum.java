package stonering.enums.unit.job;

import stonering.entity.job.Task;
import stonering.entity.unit.aspects.job.JobsAspect;

/**
 * Holds all possible jobs and experience values for level-ups.
 * Some {@link Task}s require enabled job, preventing unit to take task, if it hasn't this job.
 * Unit's jobs stored in {@link JobsAspect}.
 *
 * @author Alexander on 08.08.2019.
 */
public enum JobsEnum {
    NONE("none"),
    MINER("miner"),
    LUMBERJACK("lumberjack"),
    HARVESTER("harvester"),
    BUILDER("builder");

    public final String NAME;

    JobsEnum(String name) {
        NAME = name;
    }
}
