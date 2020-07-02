package stonering.enums.unit;

import java.util.Arrays;
import java.util.HashSet;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexander on 7/2/2020
 */
public enum PredefinedJobsEnum {
    NONE("none", null),
    MINER("miner", "mining"),
    LUMBERJACK("lumberjack", "lumbering"),
    HARVESTER("herbalist", "herbalism"),
    HAULER("hauler", null),
    BUILDER("builder", "building");

    public final String NAME;
    public final String SKILL;

    public static Set<String> set = new HashSet<>();
    public static Map<String, PredefinedJobsEnum> map = Arrays.stream(PredefinedJobsEnum.values())
            .collect(Collectors.toMap(Enum::toString, job -> job));

    PredefinedJobsEnum(String name, String skill) {
        NAME = name;
        SKILL = skill;
    }

    PredefinedJobsEnum(String name) {
        this(name, null);
    }

    public Job toJob() {
        return new Job(NAME, SKILL);
    }
}
