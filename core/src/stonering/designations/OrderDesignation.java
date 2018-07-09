package stonering.designations;

import stonering.enums.designations.DesignationTypes;
import stonering.global.utils.Position;

/**
 * Designation of order to be stored and drawn on map. Exact order specified by type field.
 *
 * @author Alexander Kuzyakov
 */
public class OrderDesignation extends Designation{


    public OrderDesignation(Position position, DesignationTypes type) {
        super(position, type);
    }
}
