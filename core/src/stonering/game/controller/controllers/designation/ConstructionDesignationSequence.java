package stonering.game.controller.controllers.designation;

import stonering.entity.job.designation.Designation;
import stonering.game.model.local_map.passage.PassageMap;

/**
 * Designation sequence for constructions like walls, floors, etc.
 * Allows to:
 * 1) Designate flat (1 z-level) box.
 * Box designation is failed if some of its parts are in different areas(see {@link PassageMap}).
 * 2) Select items to build constructions from
 * Then fills box with {@link Designation}s for construction.
 *
 * @author Alexander on 29.11.2019.
 */
public class ConstructionDesignationSequence extends DesignationSequence {

    @Override
    public void start() {

    }

    @Override
    public void end() {

    }

    @Override
    public void reset() {

    }
}
