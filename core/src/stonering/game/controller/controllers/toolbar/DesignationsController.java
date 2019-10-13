package stonering.game.controller.controllers.toolbar;

import stonering.game.controller.controllers.Controller;
import stonering.game.controller.controllers.designation.DesignationSequence;

/**
 * Controller for designating various tasks. Used as adapter for designation sequences.
 * Leaf buttons in toolbar menus create {@link DesignationSequence} instances and start them.
 * On start, sequences create first actor for toolbar.
 * After completion, sequences reset their state for repeating designation.
 * On cancelling sequences remove their actors from toolbar, so last menu is first actor again.
 *
 * @author Alexander Kuzyakov on 24.12.2017.
 */
public class DesignationsController extends Controller {

    //private DesignationTypeEnum activeDesignation;
    private DesignationSequence sequence;

    public void init() {
        super.init();
    }

    /**
     * Resets controller state.
     */
    public void handleCancel() {
        stopSequence();
    }

    /**
     * Sets sequence for controller.
     * Saves chosen designation type to be stored between events of starting and finishing designation rectangle and item selection.
     */
    public void setSequence(DesignationSequence sequence) {
        if (this.sequence != null) this.sequence.end();
        this.sequence = sequence;
    }

    public void startSequence() {
        if (sequence != null) sequence.start();
    }

    public void stopSequence() {
        if (sequence == null) return;
        sequence.end();
        sequence = null;
    }
}
