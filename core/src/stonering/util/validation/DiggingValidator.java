package stonering.util.validation;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Validates digging in some position.
 *
 * @author Alexander on 20.01.2020.
 */
public class DiggingValidator implements PositionValidator {
    BlockTypesEnum targetBlockType;

    public DiggingValidator(BlockTypesEnum targetBlockType) {
        this.targetBlockType = targetBlockType;
    }

    @Override
    public boolean validate(Position position) {
        BlockTypesEnum block = GameMvc.model().get(LocalMap.class).getBlockTypeEnumValue(position);
        return targetBlockType.OPENNESS > block.OPENNESS;
    }
}
