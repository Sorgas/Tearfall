package stonering.enums.unit.race;

import java.util.HashMap;
import java.util.List;

import stonering.enums.unit.race.CombinedAppearance;

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
    public HashMap<String, Float> statMap;
}
