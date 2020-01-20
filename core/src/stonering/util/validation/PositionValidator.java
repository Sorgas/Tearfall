package stonering.util.validation;

import stonering.util.geometry.Position;

import java.util.function.Consumer;

/**
 * Used to validate position with some logic.
 *
 * @author Alexander on 23.11.2018.
 */
@FunctionalInterface
public interface PositionValidator {

    boolean validate(Position position);

    default boolean validateAnd(Position position, Consumer<Boolean> resultConsumer) {
        boolean result = validate(position);
        if(resultConsumer != null) resultConsumer.accept(result);
        return result;
    }

    default void doIfSuccess(Position position, Consumer<Position> consumer) {
        if(validate(position)) consumer.accept(position);
    }
}
