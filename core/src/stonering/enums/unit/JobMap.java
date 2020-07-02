package stonering.enums.unit;

import java.util.*;
import java.util.stream.Collectors;

import com.badlogic.gdx.utils.Json;

import stonering.entity.unit.aspects.job.JobsAspect;
import stonering.util.global.FileUtil;

/**
 * Holds all possible jobs. Jobs can be enabled for units, allowing them to take tasks of these jobs.
 * Some jobs are predefined, and some are loaded from files.
 * Unit's jobs stored in {@link JobsAspect}.
 * See {@link SkillsMap}
 * TODO replace with set of loaded strings.
 *
 * @author Alexander on 08.08.2019.
 */
public class JobMap {
    private static JobMap instance;
    private Map<String, Job> map;

    public static JobMap instance() {
        return instance == null 
                ? instance = new JobMap()
                : instance;
    }

    private JobMap() {
        loadFromJson();
        loadPredefined();
    }

    private void loadFromJson() {
        Json json = new Json();
        List<Job> jobs = json.fromJson(ArrayList.class, Job.class, FileUtil.JOBS_PATH);
        jobs.forEach(job -> map.put(job.name, job));
    }

    private void loadPredefined() {
        putNewJob("Miner", "miner");
        putNewJob("Lumberjack", "lumberjack");
        putNewJob("Harvester", "harvester");
        putNewJob("Builder", "builder");
    }
    
    private void putNewJob(String name, String skillName) {
        if(SkillsMap.getSkill(skillName) == null) skillName = null;
        map.put(name, new Job(name, skillName));
    }
    
    public static Job get(String name) {
        return instance().map.get(name);
    }
}
