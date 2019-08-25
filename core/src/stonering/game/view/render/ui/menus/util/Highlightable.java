package stonering.game.view.render.ui.menus.util;

/**
 * Shows that widget with this interface can be highlighted.
 * If handler should be set externally, getter and setter should be overridden.
 * <p>
 * How to use: Use updateHighlighting method in widget's act method, passing highlighting state.
 *
 * @author Alexander
 */
public interface Highlightable {

    default void updateHighlighting(boolean value) {
        if (getHighlightHandler() != null) getHighlightHandler().accept(value);
    }

    default void setHighlightHandler(HighlightHandler handler) {
    }

    /**
     * Ensures, that highlightHandler will be extended.
     */
    HighlightHandler getHighlightHandler();

    /**
     * Handles highlighting.
     * Can be used for actors with children, when actor itself has no state.
     */
    abstract class HighlightHandler {
        protected void accept(Boolean newValue) {
            handle();
        }

        public abstract void handle();
    }

    /**
     * Stores old value and updates highlighting only if it changes.
     * Can be used for actors with no children.
     */
    abstract class CheckHighlightHandler extends HighlightHandler {
        protected boolean value = false;

        /**
         * Handle method is called only if the value changes.
         */
        @Override
        public void accept(Boolean newValue) {
            if (checkValue(newValue)) handle();
        }

        /**
         * Checks if new value differs from old one. Updates old value.
         *
         * @return true, if values was different, false, if equal.
         */
        private boolean checkValue(boolean newValue) {
            if(value != newValue) {
                value = newValue;
                System.out.println("value updated to " + value);
                return true;
            }
            return false;
        }
    }
}
