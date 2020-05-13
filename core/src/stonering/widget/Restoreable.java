package stonering.widget;

/**
 * Widgets with this interface can save and restore their state on demand.
 * Used to restore widgets in {@link TabbedPane}.
 *
 * @author Alexander on 5/13/2020
 */
public interface Restoreable {

    /**
     * Should save state
     */
    void saveState();

    /**
     * Should restore state
     */
    void restoreState();
}
