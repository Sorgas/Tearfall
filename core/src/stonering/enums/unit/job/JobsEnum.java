package stonering.enums.unit.job;

import java.util.*;
import java.util.stream.Collectors;

import stonering.entity.job.Task;
import stonering.entity.unit.aspects.job.JobsAspect;

/**
 * Holds all possible jobs and experience values for level-ups.
 * Some {@link Task}s require enabled job, preventing unit to take task, if it hasn't this job.
 * Unit's jobs stored in {@link JobsAspect}.
 * Performing tasks of some jobs can give experience in certain skills (defined in {@link stonering.entity.job.action.Action})
 * TODO replace with set of loaded strings.
 *
 * @author Alexander on 08.08.2019.
 */
public enum JobsEnum {
    NONE(),
    MINER(),
    LUMBERJACK(),
    HARVESTER(),
    COOK(),
    BUILDER();

    public static Set<String> set = new HashSet<>();
    public static Map<String, JobsEnum> map = Arrays.stream(JobsEnum.values())
            .collect(Collectors.toMap(Enum::toString, job -> job));
}
