package stonering.util.lang;

/**
 * Interface for objects that should be inited after creation.
 * Used for entity containers.
 * TODO reduce usage of this as much as possible.
 *
 * @author Alexander on 04.02.2019.
 */
public interface Initable {
    void init();
}
