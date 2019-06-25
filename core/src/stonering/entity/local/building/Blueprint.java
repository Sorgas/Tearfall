package stonering.entity.local.building;

import stonering.entity.local.crafting.CommonComponent;
import stonering.util.global.Initable;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Blueprint describes how building are built. Stores materials and amount, button place in menu hierarchy,
 * placing requirements, required skill and job.
 */
public class Blueprint implements Initable {
    private String name; // blueprint id.
    private String building; //building id
    private String title; // button title
    private String placing; // maps to position validator for place selecting and task checking.
    private List<String> menuPath; // button path in toolbar
    private List<CommonComponent> components; // red from json
    private LinkedHashMap<String, CommonComponent> mappedComponents; //

    @Override
    public void init() {
        mappedComponents = new LinkedHashMap<>();
        for (CommonComponent component : components) {
            component.init();
            mappedComponents.put(component.getName(), component);
        }
    }

    public CommonComponent getStepByPartName(String partName) {
        return mappedComponents.get(partName);
    }

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

    public String getPlacing() {
        return placing;
    }

    public void setPlacing(String placing) {
        this.placing = placing;
    }

    public List<CommonComponent> getComponents() {
        return components;
    }

    public void setComponents(List<CommonComponent> components) {
        this.components = components;
    }
}
