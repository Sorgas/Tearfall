package stonering.game.model.entity_selector.aspect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.enums.OrientationEnum;
import stonering.enums.buildings.BuildingType;
import stonering.util.geometry.Int2dBounds;
import stonering.util.geometry.IntVector2;
import stonering.util.geometry.RotationUtil;

/**
 * Aspect to use when player is selecting place for building.
 * Contains type of selected building,
 * current orientation and sprite,
 * defines center of its sprite to use as offset,
 * contains relative bounds of area to validate(building size).
 *
 * @author Alexander on 11.03.2020.
 */
public class BuildingSelectionAspect extends Aspect {
    public BuildingType type; // is set if selecting place for building
    public OrientationEnum orientation;
    public IntVector2 northCenter;
    public IntVector2 center; // offset of sprite
    public Int2dBounds northBounds;
    public Int2dBounds bounds; // offsets of validation box corners
    public TextureRegion region;

    public BuildingSelectionAspect(Entity entity) {
        super(entity);
    }

    /**
     * Updstes sprite, size, center, 
     * @param type
     */
    public void setBuildingType(BuildingType type) {
        this.type = type;
        northCenter = new IntVector2(type.size.x / 2, type.size.y / 2);
        northBounds = new Int2dBounds(northCenter.x, - northCenter.y, type.size.x - northCenter.x, type.size.y - northCenter.y);
        setOrientation(OrientationEnum.N);
        region = type.getSprite(OrientationEnum.N);
    } 
    
    public void setOrientation(OrientationEnum orientation) {
        if(type == null) return;
        this.orientation = orientation;
        center = RotationUtil.rotateVector(northCenter, orientation);
    }
    
//    private int[] normalizeBySize() {
//        
//    }
//    
//    private void defineCenter(OrientationEnum orientation) {
//    }
}
