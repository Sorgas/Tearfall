package stonering.enums.buildings.blueprint;

import java.util.ArrayList;
import java.util.List;

/**
 * Blueprint class for reading json into.
 *
 * @author Alexander on 18.09.2019.
 */
public class RawBlueprint {
    public String name; // blueprint id.
    public String building; //building id
    public String title; // button name
    public String placing = "floor"; // maps to position validator for place selecting and task checking.
    public List<String> menuPath = new ArrayList<>(); // button path in toolbar
    public List<String> ingredients = new ArrayList<>();
    public String icon;
}
