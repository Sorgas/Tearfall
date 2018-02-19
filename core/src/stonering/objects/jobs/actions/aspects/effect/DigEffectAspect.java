package stonering.objects.jobs.actions.aspects.effect;

import stonering.enums.blocks.BlockTypesEnum;
import stonering.enums.designations.DesignationTypes;
import stonering.enums.materials.Material;
import stonering.enums.materials.MaterialMap;
import stonering.game.core.model.GameContainer;
import stonering.game.core.model.LocalMap;
import stonering.generators.items.DiggingProductGenerator;
import stonering.global.utils.Position;
import stonering.objects.jobs.actions.Action;

public class DigEffectAspect extends EffectAspect {
    private DesignationTypes designationType;
    private GameContainer container;

    public DigEffectAspect(Action action, DesignationTypes designationType) {
        super(action);
        container = action.getGameContainer();
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
        Material material = MaterialMap.getInstance().getMaterial(container.getLocalMap().getMaterial(action.getTargetPosition()));
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
        leaveStone(material);
        action.finish();
    }

    private void validateAndChangeBlock(Position pos, BlockTypesEnum type) {
        boolean valid = false;
        LocalMap map = container.getLocalMap();
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

    private void leaveStone(Material material) {
        DiggingProductGenerator generator = new DiggingProductGenerator();
        if (generator.productRequired(material))
            container.getItemContainer().addItem(generator.generateDigProduct(material), action.getTargetPosition());
    }

    public DesignationTypes getBlockType() {
        return designationType;
    }

    public void setBlockType(DesignationTypes blockType) {
        this.designationType = blockType;
    }
}
