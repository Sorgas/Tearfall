package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.util.geometry.Position;

/**
 * @author Alexander on 23.06.2020.
 */
public class DiggingChannelValidator extends DiggingValidator {

    public DiggingChannelValidator() {
        super(BlockTypeEnum.SPACE);
    }

    @Override
    public Boolean apply(Position position) {
        return super.apply(position) && position.z > 0;
    }
}
