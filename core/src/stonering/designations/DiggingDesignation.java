package stonering.designations;

import stonering.enums.designations.DesignationTypes;
import stonering.global.utils.Position;

/**
 * @author Alexander Kuzyakov
 * created on 26.06.2018
 */
public class DiggingDesignation extends Designation {
    private DesignationTypes type;

    public DiggingDesignation(Position position, DesignationTypes type) {
        super(position);
        this.type = type;
    }

    public DesignationTypes getType() {
        return type;
    }

    public void setType(DesignationTypes type) {
        this.type = type;
    }
}
