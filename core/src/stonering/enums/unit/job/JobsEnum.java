package stonering.enums.unit.job;

import stonering.entity.job.Task;
import stonering.entity.unit.aspects.job.JobsAspect;

/**
 * Holds all possible jobs and experience values for level-ups.
 * Some {@link Task}s require enabled job, preventing unit to take task, if it hasn't this job.
 * Unit's jobs stored in {@link JobsAspect}.
 * Performing tasks of some jobs can give experience in certain skills (defined in {@link stonering.entity.job.action.Action})
 *
 * @author Alexander on 08.08.2019.
 */
public enum JobsEnum {
    NONE(),
    MINER(),
    LUMBERJACK(),
    HARVESTER(),
    BUILDER();
}
