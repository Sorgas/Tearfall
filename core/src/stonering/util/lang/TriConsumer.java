package stonering.util.lang;

/**
 * Consumer for three arguments
 *
 * @author Alexander on 10.06.2020.
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void accept(A a, B b, C c);
}
