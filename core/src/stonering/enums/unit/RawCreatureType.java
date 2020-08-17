package stonering.enums.unit;

import java.util.List;

/**
 * @author Alexander on 29.05.2019.
 */
public class RawCreatureType {
    public String name;
    public String title;
    public String description;
    public String bodyTemplate;
    public List<String> limbsToCover;
    public List<String> aspects;
    public int baseHP;
    public int[] atlasXY;
    public String color;
    public CombinedAppearance combinedAppearance;
}
