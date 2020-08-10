package stonering.entity.unit.aspects.body;

import java.util.ArrayList;
import java.util.List;

import stonering.enums.unit.health.HealthFunctionEnum;
import stonering.util.global.Pair;

/**
 * Whole body disease. Diseases progress from 0 to 1 if not cured.
 * Penalties applied proportionally during progression.
 * 
 * @author Alexander on 10.08.2020.
 */
public class Disease extends HealthEffect {
    public final List<Pair<HealthFunctionEnum, Float>> targetPenalties = new ArrayList<>();
    public float progress = 0;
    
    @Override
    public void apply() {
        
    }

    @Override
    public void unapply() {

    }
}
