package stonering.entity.local.building;

import java.util.List;

/**
 * Blueprint describes how building are built. Stores materials and amount, button place in menu hierarchy,
 * placing requirements, required skill and job.
 */
public class Blueprint {
    private String name; // blueprint id.
    private String building; //building id
    private String title; // button title
    private List<String> menuPath; // button path in toolbar


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(List<String> menuPath) {
        this.menuPath = menuPath;
    }

}
