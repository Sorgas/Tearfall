package stonering.enums.buildings;

import java.util.ArrayList;
import java.util.List;

/**
 * Red from buildings/*.json
 *
 * @author Alexander on 12.03.2020
 */
public class RawBuildingType {
    public String building; // id
    public String title;
    public String description;
    public List<String> aspects = new ArrayList<>();
    public List<String> parts = new ArrayList<>();
    public String passage = "wall";
    public boolean construction = false;
    public List<String> recipes = new ArrayList<>(); // filled from crafting/lists.json
    public int[] size = {1, 1}; // width/height for N orientation
    public int[][] access = {}; // most buildings have no access point
    public int[][] NSEWsprites = {{0, 0}, {0, 0}, {0, 0}, {0, 0},}; // NSEW
    public int[] atlasXY = {0, 0};
    public String color = "0xffffffff";
}
