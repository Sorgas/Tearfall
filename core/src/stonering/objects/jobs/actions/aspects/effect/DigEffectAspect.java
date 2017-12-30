package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTypes;
import stonering.global.utils.Position;

public class DigEffectAspect extends EffectAspect {
    private DesignationsTypes designationType;

    public DigEffectAspect(DesignationsTypes designationType) {
        this.designationType = designationType;
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
        int material = gameContainer.getLocalMap().getMaterial(position);
        switch (designationType) {
            case DIG: {
                gameContainer.getLocalMap().setBlock(position, BlockTypesEnum.FLOOR, material);
            }
        }
        action.finish();
    }

    public DesignationsTypes getBlockType() {
        return designationType;
    }

    public void setBlockType(DesignationsTypes blockType) {
        this.designationType = blockType;
    }
}
