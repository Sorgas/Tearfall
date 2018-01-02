package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationsTypes;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
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
        if (workAmount <= 0) {
            finish();
        }
    }

    private void finish() {
        Position pos = action.getTargetAspect().getTargetPosition();
        switch (designationType) {
            case DIG: {
                validateAndChangeBlock(pos, BlockTypesEnum.FLOOR);
                break;
            }
            case RAMP: {
                validateAndChangeBlock(pos, BlockTypesEnum.RAMP);
                validateAndChangeBlock(new Position(pos.getX(), pos.getY(), pos.getZ() + 1), BlockTypesEnum.SPACE);
                break;
            }
            case STAIRS: {
                validateAndChangeBlock(pos, BlockTypesEnum.STAIRS);
                break;
            }
            case CHANNEL: {
                validateAndChangeBlock(pos, BlockTypesEnum.SPACE);
                validateAndChangeBlock(new Position(pos.getX(), pos.getY(), pos.getZ() - 1), BlockTypesEnum.RAMP);
                break;
            }
        }
        action.finish();
    }

    private void validateAndChangeBlock(Position pos, BlockTypesEnum type) {
        boolean valid = false;
        LocalMap map = gameContainer.getLocalMap();
        switch (type) {
            case RAMP:
            case STAIRS:
                valid = map.getBlockType(pos) == BlockTypesEnum.WALL.getCode();
                break;
            case FLOOR:
                valid = map.getBlockType(pos) == BlockTypesEnum.WALL.getCode() ||
                        map.getBlockType(pos) == BlockTypesEnum.RAMP.getCode() ||
                        map.getBlockType(pos) == BlockTypesEnum.STAIRS.getCode();
                break;
            case SPACE:
                valid = true;
        }
        if (valid) {
            map.setBlocType(pos, type.getCode());
        }
    }

    public DesignationsTypes getBlockType() {
        return designationType;
    }

    public void setBlockType(DesignationsTypes blockType) {
        this.designationType = blockType;
    }
}
