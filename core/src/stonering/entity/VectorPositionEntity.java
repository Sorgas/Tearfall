package stonering.entity;

import com.badlogic.gdx.math.Vector3;
import stonering.util.geometry.Position;

/**
 * Extends regular entity with integer position.
 * Used for drawing units and items 'between' cells.
 *
 * @author Alexander on 21.10.2019.
 */
public abstract class VectorPositionEntity extends Entity {
    public Vector3 vectorPosition;

    protected VectorPositionEntity() {
        super();
        vectorPosition = new Vector3();
    }

    public VectorPositionEntity(Position position) {
        super(position);
        vectorPosition = new Vector3(position.x, position.y, position.z);
    }

    @Override
    public void setPosition(Position position) {
        super.setPosition(position);
        vectorPosition.set(position.x, position.y, position.z);
    }

    public void setPosition(Vector3 vector) {
        vectorPosition.set(vector);
        position.set(vector);
    }
}
