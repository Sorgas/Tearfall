package stonering.enums.items.type.raw;

import stonering.enums.items.type.ItemPartType;
import stonering.enums.items.type.ToolItemType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexander_Kuzyakov on 13.06.2019.
 */
public class RawItemType {
    public String name = "unset_item_type"; // id
    public String baseItem; // items can extends other items
    public String title = ""; // displayable name
    public String description; // displayable description
    public ToolItemType tool; // is set if this item could be used as tool
    public List<ItemPartType> parts; // defines parts of item. first one is main

    // first element of lists is aspect name
    public List<String> typeAspects = new ArrayList<>(); // constant aspects. stored in type (value, resource)
    public List<String> aspects = new ArrayList<>(); // other aspects, item aspects filled from this on generation.

    // render
    public int[] atlasXY;
    public String color;
}
