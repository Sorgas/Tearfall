package stonering.enums.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.RotationUtil;

import java.util.*;

/**
 * Type of {@link Building}.
 * Type contains unrotated(N) size and map of passage.
 * Sprites are specified with group of four orientation sprites, and common offset in atlas.
 * Size is given in tiles with default 1:1;
 * Determines usage and appearance.
 *
 * @author Alexander Kuzyakov on 28.06.2018
 */
public class BuildingType {
    public String building; // id
    public String title;
    public String description;
    public List<List<String>> aspects;
    public List<String> parts;
    public String passage = "X";
    public PassageEnum[][] passageArray; // array for N orientation
    public boolean construction = false;
    public List<String> recipes; // filled from crafting/lists.json
    public int[] size = {1, 1}; // width/height for N orientation
    public int[][] sprites = {{0, 0}, {0, 1}, {1, 0}, {1, 1}}; // NSEW
    public int[] atlasXY = {0, 0};
    public String color = "0xffffffff";

    public BuildingType() {
        aspects = new ArrayList<>();
        parts = new ArrayList<>();
        recipes = new ArrayList<>();
    }

    public TextureRegion getSprite(OrientationEnum orientation) {
        return AtlasesEnum.buildings.getRegion(sprites[orientation.ordinal()], RotationUtil.orientSize(size, orientation));
    }
}
