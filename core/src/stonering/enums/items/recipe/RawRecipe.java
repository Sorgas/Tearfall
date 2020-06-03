package stonering.enums.items.recipe;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for reading from json, as recipe parts are stores as string arrays.
 *
 * @author Alexander_Kuzyakov on 23.05.2019.
 */
public class RawRecipe {
    public String name;
    public String title;
    public String itemName;
    public String newMaterial;
    public String iconName;
    public String description;

    public List<String> ingredients = new ArrayList<>();
    public String newTag;
    public String removeTag;
    public float workAmount; // increases crafting time
    public String job;
    public String skill;
}
