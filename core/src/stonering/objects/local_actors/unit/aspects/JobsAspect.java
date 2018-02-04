package stonering.objects.local_actors.unit.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;

import java.util.ArrayList;

/**
 * Created by Alexander on 31.01.2018.
 *
 * stores jobs enabled for unit
 */
public class JobsAspect extends Aspect {
    private ArrayList<String> jobs;

    public JobsAspect(String name, AspectHolder aspectHolder) {
        super(name, aspectHolder);
        jobs = new ArrayList<>();
    }

    public ArrayList<String> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<String> jobs) {
        this.jobs = jobs;
    }
}
