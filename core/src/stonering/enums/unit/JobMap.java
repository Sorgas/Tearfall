package stonering.enums.unit;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import stonering.entity.unit.aspects.job.JobSkillAspect;
import stonering.util.lang.FileUtil;
import stonering.util.logging.Logger;

/**
 * Holds all possible jobs. Jobs can be enabled for units, allowing them to take tasks of these jobs.
 * Some jobs are predefined, and some are loaded from files.
 * Unit's jobs stored in {@link JobSkillAspect}.
 * See {@link SkillMap}
 * TODO replace with set of loaded strings.
 *
 * @author Alexander on 08.08.2019.
 */
public class JobMap {
    private static JobMap instance;
    private final Map<String, Job> map;
    private final List<Job> skilled;
    private final List<Job> unskilled;

    public static JobMap instance() {
        return instance == null ? instance = new JobMap() : instance;
    }

    private JobMap() {
        map = new HashMap<>();
        loadFromJson();
        loadPredefined();
        skilled = map.values().stream().filter(job -> job.skill != null).collect(Collectors.toList());
        unskilled = map.values().stream().filter(job -> job.skill == null).collect(Collectors.toList());
    }

    private void loadFromJson() {
        Json json = new Json();
        FileHandle file = new FileHandle(FileUtil.JOBS_PATH);
        List<Job> jobs = json.fromJson(ArrayList.class, Job.class, file);
        jobs.forEach(job -> putNewJob(job.name, job.skill));
    }

    private void loadPredefined() {
        for (PredefinedJobsEnum job : PredefinedJobsEnum.values()) {
            putNewJob(job.NAME, job.SKILL);
        }
    }

    private void putNewJob(String name, String skill) {
        if (skill != null && SkillMap.getSkill(skill) == null) {
            Logger.LOADING.logWarn("Skill with name " + skill + " not found. No skill will be associated with job " + name);
            skill = null;
        }
        map.put(name, new Job(name, skill));
    }

    public static Job get(String name) {
        return instance().map.get(name);
    }

    public static Collection<Job> all() {
        return instance().map.values();
    }

    public static List<Job> skilled() {
        return instance().skilled;
    }

    public static List<Job> unskilled() {
        return instance().unskilled;
    }
    
    public static Job bySkill(String name) {
        return skilled().stream()
                .filter(job -> name.equals(job.skill))
                .findFirst()
                .orElse(null);
    }
}
