package stonering.enums.unit.race;

import java.util.ArrayList;
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
    public List<String> desiredSlots = new ArrayList<>();
    public List<String> aspects = new ArrayList<>();
    public int[] atlasXY;
    public String color;
    public CombinedAppearance combinedAppearance;
    public HashMap<String, Float> statMap = new HashMap<>();
}
