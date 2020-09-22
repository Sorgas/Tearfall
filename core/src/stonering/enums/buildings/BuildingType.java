package stonering.enums.buildings;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.building.Building;
import stonering.enums.OrientationEnum;
import stonering.enums.blocks.BlockTypeEnum;
import stonering.enums.blocks.PassageEnum;
import stonering.stage.renderer.atlas.AtlasesEnum;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.RotationUtil;

import java.util.*;
import java.util.stream.Collectors;

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
    public Map<String, List<String>> aspects;
    public List<String> parts;
    public List<String> recipes; // filled from crafting/lists.json
    public IntVector2 size; // width/height for N orientation
    public List<IntVector2> access;
    public PassageEnum passage;
    public boolean construction = false; // TODO to remove
    public String atlasName;
    public IntVector2[] NSEWsprites;
    public String color = "0xffffffff";

    public BuildingType() {
        
    }

    public BuildingType(RawBuildingType raw) {
        building = raw.building;
        title = raw.title;
        description = raw.description;
        access = Arrays.stream(raw.access).map(IntVector2::new).collect(Collectors.toList());
        aspects = new HashMap<>();
        parts = new ArrayList<>(raw.parts);
        passage = BlockTypeEnum.getType(raw.passage).PASSING;
        recipes = new ArrayList<>(raw.recipes);
        size = new IntVector2(raw.size[0], raw.size[1]);
        NSEWsprites = new IntVector2[4]; // for four orientations
    }

    public TextureRegion getSprite(OrientationEnum orientation) {
        return AtlasesEnum.buildings.getBlockTile(atlasName, NSEWsprites[orientation.ordinal()], RotationUtil.orientSize(size, orientation));
    }
}
