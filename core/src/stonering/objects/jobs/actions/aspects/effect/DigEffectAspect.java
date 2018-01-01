package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTypes;
import stonering.game.core.model.GameContainer;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public class DigEffectAspect extends EffectAspect {
    private DesignationsTypes designationType;

    public DigEffectAspect(Action action, GameContainer gameContainer, DesignationsTypes designationType) {
        super(action, gameContainer);
        this.designationType = designationType;
        this.workAmount = 100;
    }

    @Override
    public void perform() {
        workAmount--;
        System.out.println("working");
        if (workAmount <= 0) {
            finish();
        }
    }

    private void finish() {
        System.out.println("work_finished");
        Position position = action.getTargetAspect().getTargetPosition();
        switch (designationType) {
            case DIG: {
                gameContainer.getLocalMap().setBlocType(position, BlockTypesEnum.FLOOR.getCode());
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
