package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.global.utils.Position;

public class DigEffectAspect extends EffectAspect {
    private BlockTypesEnum blockType;

    @Override
    public void perform() {
        workAmount--;
        if(workAmount <= 0) {
            finish();
        }
    }

    private void finish() {
        Position position = action.getTargetAspect().getTargetPosition();
        int material = 0;
        if (blockType != BlockTypesEnum.SPACE)
            material = gameContainer.getLocalMap().getMaterial(position);
        gameContainer.getLocalMap().setBlock(position, blockType, material);
        action.finish();
    }

    public BlockTypesEnum getBlockType() {
        return blockType;
    }

    public void setBlockType(BlockTypesEnum blockType) {
        this.blockType = blockType;
    }
}
