package stonering.entity;

import com.badlogic.gdx.math.Vector3;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 21.10.2019.
 */
public abstract class FloatPositionEntity extends Entity {
    public Vector3 vectorPosition;

    protected FloatPositionEntity() {
        super();
        vectorPosition = new Vector3();
    }

    public FloatPositionEntity(Position position) {
        super(position);
        vectorPosition.set(position.x, position.y, position.z);
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        vectorPosition.set(position.x, position.y, position.z);
    }
}
