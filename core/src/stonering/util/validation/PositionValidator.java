package stonering.util.validation;

import org.jetbrains.annotations.NotNull;
import stonering.util.geometry.Position;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Used to validate position with some logic.
 *
 * @author Alexander on 23.11.2018.
 */
public interface PositionValidator extends Function<Position, Boolean> {

    default boolean validateAnd(Position position, @NotNull Consumer<Boolean> resultConsumer) {
        boolean result = apply(position);
        resultConsumer.accept(result);
        return result;
    }

    default void doIfSuccess(Position position, Consumer<Position> consumer) {
        if(apply(position)) consumer.accept(position);
    }
}
