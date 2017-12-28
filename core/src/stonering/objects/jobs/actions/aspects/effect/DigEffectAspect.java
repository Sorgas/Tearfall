package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTypes;
import stonering.global.utils.Position;

public class DigEffectAspect extends EffectAspect {
    private DesignationsTypes blockType;

    public DigEffectAspect(DesignationsTypes blockType) {
        this.blockType = blockType;
    }

    @Override
    public void perform() {
        workAmount--;
        if (workAmount <= 0) {
            finish();
        }
    }

    private void finish() {
        Position position = action.getTargetAspect().getTargetPosition();
        int material = 0;
        switch (blockType) {
            case DIG: {
                material = gameContainer.getLocalMap().getMaterial(position);
                gameContainer.getLocalMap().setBlock(position, BlockTypesEnum.FLOOR, material);
            }
        }
        action.finish();
    }

    public DesignationsTypes getBlockType() {
        return blockType;
    }

    public void setBlockType(DesignationsTypes blockType) {
        this.blockType = blockType;
    }
}
