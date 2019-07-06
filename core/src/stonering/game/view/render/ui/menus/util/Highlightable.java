package stonering.game.view.render.ui.menus.util;

import javax.xml.transform.Source;

/**
 * Shows that widget with this interface can be highlighted.
 * If handler should be set externally, getter and setter should be overridden.
 *
 * @author Alexander
 */
public interface Highlightable {

    default void updateHighlighting(boolean value) {
        if (getHighlightHandler() != null) getHighlightHandler().accept(value);
    }

    default void setHighlightHandler(HighlightHandler handler) {
    }

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
