package stonering.enums.unit;

/**
 * Job represents group of tasks that unit can perform, if job is enabled for unit.
 * If job name is set, unit will gain experience in that skill and benefit from it. 
 * 
 * @author Alexander on 02.07.2020.
 */
public class Job {
    public String name;
    public String skill;
    public String icon;
    public int index; // order for unit menu and common jobs menu

    public Job() {
    }

    public Job(String name, String skill) {
        this.name = name;
        this.skill = skill;
    }
}
