package stonering.enums.buildings;

import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.stage.renderer.TileSpriteDescriptor;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

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
    public int[] size = {1, 1}; // width/height
    public EnumMap<OrientationEnum, int[]> sprites;
    public int[] atlasXY;
    public String color = "0xffffffff";

    public BuildingType() {
        aspects = new ArrayList<>();
        parts = new ArrayList<>();
        recipes = new ArrayList<>();
        sprites = new EnumMap<>(OrientationEnum.class);
        sprites.put(N, new int[]{0, 0});
        sprites.put(S, new int[]{1, 0});
        sprites.put(W, new int[]{0, 1});
        sprites.put(E, new int[]{1, 1});
    }
    
    public TileSpriteDescriptor
}
