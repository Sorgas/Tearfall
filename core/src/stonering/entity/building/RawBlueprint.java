package stonering.entity.building;

import stonering.entity.crafting.BuildingComponent;
import java.util.List;

/**
 * @author Alexander on 18.09.2019.
 */
public class RawBlueprint {
    public String name; // blueprint id.
    public String building; //building id
    public String title; // button name
    public String placing; // maps to position validator for place selecting and task checking.
    public List<String> menuPath; // button path in toolbar
    public List<BuildingComponent> components; // red from json
}
