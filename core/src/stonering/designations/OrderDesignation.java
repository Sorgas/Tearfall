package stonering.designations;

import stonering.enums.designations.DesignationTypeEnum;
import stonering.util.geometry.Position;

/**
 * Designation of order to be stored and rendered on map. Exact order specified by type field.
 *
 * @author Alexander Kuzyakov
 */
public class OrderDesignation extends Designation{

    public OrderDesignation(Position position, DesignationTypeEnum type) {
        super(position, type);
    }
}
