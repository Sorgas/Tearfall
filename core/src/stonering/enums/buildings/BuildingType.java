package stonering.enums.buildings;

import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.stage.renderer.TileSpriteDescriptor;

import java.util.*;

import static stonering.enums.OrientationEnum.*;

/**
 * Type of {@link Building}.
 * Sprites are specified with group of four orientation sprites, and common offset in atlas.
 * Size is given in tiles with default 1:1;
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
    public String passage = "wall"; // points to block type
    public boolean construction = false;
    public List<String> recipes; // filled from crafting/lists.json
    public int[] size = {1, 1}; // width/height for N orientation
    public int[][] sprites = {};
    public int[] atlasXY;
    public String color = "0xffffffff";

    public BuildingType() {
        aspects = new ArrayList<>();
        parts = new ArrayList<>();
        recipes = new ArrayList<>();
    }
}
