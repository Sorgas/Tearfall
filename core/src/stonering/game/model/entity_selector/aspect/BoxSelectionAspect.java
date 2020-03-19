package stonering.game.model.entity_selector.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.game.model.entity_selector.EntitySelector;
import stonering.util.geometry.Int3dBounds;
import stonering.util.geometry.Position;

import java.util.function.Consumer;

/**
 * Aspect of {@link EntitySelector} for providing logic of frame selection.
 * When selection is started, LMB pressed, or E key pressed, frame starts and boxStart gets current value of selector position.
 * When LMB is released, or E pressed again, box defined by boxStart and selector position can be used for selection.
 * On Q or RMB press, boxStart is cleared.
 * 
 * @author Alexander on 16.03.2020.
 */
public class BoxSelectionAspect extends Aspect {
    public boolean boxEnabled = true;
    public Position boxStart; // this with selector's position are opposite corners of selection box
    public final Consumer<Consumer<Position>> boxIterator;

    public BoxSelectionAspect(Entity entity) {
        super(entity);
        boxIterator = consumer -> getBox().iterate(consumer);
    }
    
    public Int3dBounds getBox() {
        return new Int3dBounds(entity.position, boxStart != null ? boxStart : entity.position);
    }
}
