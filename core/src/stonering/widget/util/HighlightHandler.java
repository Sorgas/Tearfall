package stonering.widget.util;

/**
 *
 *
 * @author Alexander on 05.03.2020
 */
public abstract class HighlightHandler {
    private boolean savedValue = false;

    protected abstract void apply(boolean value);

    public void accept(boolean value) {
        if(savedValue != value) apply(savedValue = value);
    }
}
