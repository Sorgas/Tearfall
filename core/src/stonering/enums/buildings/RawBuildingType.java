package stonering.enums.buildings;

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
    public List<List<String>> aspects;
    public List<String> parts;
    public String passage = "X";
    public boolean construction = false;
    public List<String> recipes; // filled from crafting/lists.json
    public int[] size = {1, 1}; // width/height for N orientation
    public int[][] sprites = {{0, 0}, {0, 1}, {1, 0}, {1, 1}}; // NSEW
    public int[] atlasXY = {0, 0};
    public String color = "0xffffffff";
}
