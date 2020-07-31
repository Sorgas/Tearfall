package stonering.enums.plants.raw;

import stonering.enums.generation.PlacingTagEnum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple bean for reading data from json files.
 *
 * @author Alexander_Kuzyakov on 30.04.2019.
 */
public class RawPlantType {
    public String name;
    public String title;
    public String materialName = "generic_plant"; // in null for substrates
    public String description;

    public int[] temperatureBounds = new int[2]; // min and max temperature
    public int[] rainfallBounds = new int[2];  // min and max painfall
    public List<RawPlantLifeStage> lifeStages = new ArrayList<>();
    public List<String> placingTags = new ArrayList<>();
    public Set<PlacingTagEnum> placingTagsSet = new HashSet<>();
    public List<Integer> plantingStart = new ArrayList<>();
    public int[] atlasXY = new int[2];
    public boolean destroyOnHarvest;
}
