package stonering.enums.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.geometry.IntVector2;
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
    public final String building; // id
    public final String title;
    public final String description;
    public List<List<String>> aspects;
    public List<String> parts;
    public List<String> recipes; // filled from crafting/lists.json
    public IntVector2 size; // width/height for N orientation
    public PassageEnum[][] passageArray; // array for N orientation
    public final IntVector2[] sprites; // NSEW
    public boolean construction = false; // TODO to remove
    public String color = "0xffffffff";

    public BuildingType(RawBuildingType raw) {
        building = raw.building;
        title = raw.title;
        description = raw.description;
        aspects = new ArrayList<>(raw.aspects);
        parts = new ArrayList<>(raw.parts);
        recipes = new ArrayList<>(raw.recipes);
        size = new IntVector2(raw.size[0], raw.size[1]);
        passageArray = new PassageEnum[size.x][size.y];
        sprites = new IntVector2[4]; // for four orientations
    }

    public TextureRegion getSprite(OrientationEnum orientation) {
        return AtlasesEnum.buildings.getRegion(sprites[orientation.ordinal()], RotationUtil.orientSize(size, orientation));
    }
}
