package stonering.entity.unit.aspects.body;

import stonering.entity.unit.Unit;

/**
 * Whole body disease. Diseases progress from 0 to 1 if not cured.
 * Penalties applied proportionally during progression.
 * 
 * @author Alexander on 10.08.2020.
 */
public abstract class Disease extends HealthEffect {
    public float progress = 0;
    public final boolean lethal;

    public Disease(boolean lethal) {
        this.lethal = lethal;
    }

    @Override
    public void apply(Unit unit) {

    }

    @Override
    public void unapply(Unit unit) {

    }
}
