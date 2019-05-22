package stonering.entity.local.building;

import java.util.ArrayList;
import java.util.List;

/**
 * Type of building.
 * Determines usage and appearance.
 *
 * @author Alexander Kuzyakov on 28.06.2018
 */
public class BuildingType {
    public String building;
    public String title;
    public String description;
    public List<List<String>> aspects;
    public List<String> parts;
    public String passage;
    public boolean construction;

    public int[] atlasXY;
    public String color;

    public List<String> recipes; // filled from crafting/lists.json

    public BuildingType() {
        aspects = new ArrayList<>();
        parts = new ArrayList<>();
        recipes = new ArrayList<>();
    }
}
