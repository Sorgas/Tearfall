package stonering.entity.local.unit.aspects;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;

import java.util.ArrayList;

/**
 * @author Alexander Kuzyakov on 31.01.2018.
 *
 * stores jobs enabled for unit
 */
public class JobsAspect extends Aspect {
    public static final String NAME = "jobs";
    private ArrayList<String> jobs;

    public JobsAspect(String name, AspectHolder aspectHolder) {
        super(aspectHolder);
        jobs = new ArrayList<>();
    }

    public ArrayList<String> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<String> jobs) {
        this.jobs = jobs;
    }

    @Override
    public String getName() {
        return NAME;
    }
}
