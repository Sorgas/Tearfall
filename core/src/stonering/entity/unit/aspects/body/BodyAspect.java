package stonering.entity.unit.aspects.body;

import stonering.entity.Aspect;
import stonering.entity.unit.Unit;
import stonering.entity.unit.aspects.health.Buff;

import java.util.*;

/**
 * Points to creature's body which is stored in creature type.
 * Stores wounds on the creature's body, which are individual.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspect extends Aspect {
    public final String bodyTemplate;
    public final List<String> bodyPartsToCover;
    public final List<Wound> wounds;
    public final Map<String, DiseaseState> diseases;
    public final Map<String, DiseaseState> needDiseases;
    public final Map<String, Buff> buffs;

    public BodyAspect(Unit unit, String bodyTemplate) {
        super(unit);
        this.bodyTemplate = bodyTemplate;
        wounds = new ArrayList<>();
        diseases = new HashMap<>();
        needDiseases = new HashMap<>();
        bodyPartsToCover = new ArrayList<>();
        buffs = new HashMap<>();
    }
    
    public float getDiseaseProgress(String name) {
        return Optional.ofNullable(diseases.get(name))
                .map(state -> state.progress)
                .orElse(0f);
    }
}
