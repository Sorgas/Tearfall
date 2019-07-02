package stonering.enums.items.type;

import java.util.List;

/**
 * @author Alexander_Kuzyakov on 13.06.2019.
 */
public class RawItemType {
    public String name;                                // id
    public String title;                               // displayable name
    public String description;                         // displayable description

    public WearItemType wear;                          // is set if this item could be worn
    public ToolItemType tool;                          // is set if this item could be used as tool
    public ContainerItemType container;                // is set if this item could contain other item

    public List<ItemPartType> parts;              // defines parts of item. first one is main

    // first element of lists is aspect name
    public List<List<String>> typeAspects; // constant aspects. stored in type (value, resource)
    public List<List<String>> aspects; // other aspects, item aspects filled from this on generation.

    // render
    public int[] atlasXY;
    public String color;
}
