package stonering.game.view.render.ui.menus.util;

/**
 * Shows that widget with this interface can be highlighted.
 * If handler should be set externally, getter and setter should be overridden.
 * General approach is to use updateHighlighting method in widget's act method.
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
     */
    abstract class HighlightHandler {
        protected void accept(Boolean newValue) {
            handle();
        }

        public abstract void handle();
    }

    /**
     * Stores old value and updates highlighting only if it changes.
     */
    abstract class CheckHighlightHandler extends HighlightHandler {
        protected static boolean value = false;

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
        private static boolean checkValue(boolean newValue) {
            return (value != newValue) && ((value = newValue) || true);
        }
    }
}
