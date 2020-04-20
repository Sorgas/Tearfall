package stonering.util.validation;

import stonering.enums.blocks.BlockTypeEnum;
import stonering.game.GameMvc;
import stonering.game.model.local_map.LocalMap;
import stonering.util.geometry.Position;

/**
 * Validates digging in some position.
 *
 * @author Alexander on 20.01.2020.
 */
public class DiggingValidator implements PositionValidator {
    public final BlockTypeEnum targetBlockType;

    public DiggingValidator(BlockTypeEnum targetBlockType) {
        this.targetBlockType = targetBlockType;
    }

    @Override
    public Boolean apply(Position position) {
        BlockTypeEnum block = GameMvc.model().get(LocalMap.class).blockType.getEnumValue(position);
        return targetBlockType.OPENNESS > block.OPENNESS;
    }
}
