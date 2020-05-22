package stonering.enums.unit;

import java.util.*;
import java.util.stream.Collectors;

import stonering.entity.unit.aspects.job.JobsAspect;

/**
 * Holds all possible jobs. Jobs can be enabled for units, allowing them to take tasks.
 * Unit's jobs stored in {@link JobsAspect}.
 * See {@link SkillsMap}
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
