package stonering.entity.local.unit.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.Entity;

import java.util.ArrayList;

/**
 * @author Alexander Kuzyakov on 31.01.2018.
 *
 * stores job enabled for unit
 */
public class JobsAspect extends Aspect {
    public static final String NAME = "job";
    private ArrayList<String> jobs;

    public JobsAspect(String name, Entity entity) {
        super(entity);
        jobs = new ArrayList<>();
    }

    public ArrayList<String> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<String> jobs) {
        this.jobs = jobs;
    }
}
