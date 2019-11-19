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
    public String category;
    public String title;
    public String itemName;
    public String description;
    public List<List<String>> parts;
    public List<List<String>> consumed = new ArrayList<>();
    public String newTag;
}
