package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.GameMvc;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;
import stonering.util.validation.PositionValidator;

import java.util.function.Consumer;

/**
 * Part of {@link EntitySelector} that contains selection box and logic for handling selection and cancellation.
 *
 * Box last position is always {@link EntitySelector}'s position.
 * Handler is called once for selection box. Box iterator can be used to do some action with each tile in the box.
 * Box iterator also uses position validator.
 *
 * @author Alexander on 10.01.2020
 */
public class SelectionAspect extends Aspect {
    public Position boxStart;
    public boolean enabled;
    public Consumer<Int3dBounds> selectHandler; // called once for the whole selection box
    public final Consumer<Consumer<Position>> boxIterator; // iterates over selection box, can be used in selection handler

    public Consumer<Position> cancelHandler;
    public PositionValidator validator; // validates each position

    public SelectionAspect(Entity entity) {
        super(entity);
        enabled = true;
        boxIterator = consumer -> getBox().iterator.accept(position -> validator.doIfSuccess(position, consumer));
    }

    public Int3dBounds getBox() {
        return new Int3dBounds(entity.position, boxStart != null ? boxStart : entity.position);
    }

    public void reset() {
        selectHandler = position -> GameMvc.instance().view().showEntityStage(getBox());
        validator = position -> true;
    }
}
