package stonering.enums.unit;

/**
 * Job represents group of tasks that unit can perform, if job is enabled for unit.
 * If job name is set, unit will gain experience in that skill and benefit from it. 
 * 
 * @author Alexander on 02.07.2020.
 */
public class Job {
    public String name;
    public String skillName;

    public Job() {
    }

    public Job(String name, String skillName) {
        this.name = name;
        this.skillName = skillName;
    }
}
