package stonering.entity.unit.aspects.body;

import stonering.entity.Aspect;
import stonering.entity.unit.Unit;
import stonering.enums.unit.body.BodyPart;
import stonering.enums.unit.health.HealthEffect;

import java.util.*;

/**
 * Stores state of creature body. Body parts are listed in {@link stonering.enums.unit.race.CreatureType}.
 * Aspect stores wounds on body parts, missing parts, diseases of a creature, and effects of diseases and wounds.
 *
 * @author Alexander Kuzyakov on 19.10.2017.
 */
public class BodyAspect extends Aspect {
    public final Map<String, String> bodyParts = new HashMap<>();
    public final Map<String, Wound> wounds = new HashMap<>(); // body part to wound
    public final Set<String> missingParts = new HashSet<>(); 
    public final Map<String, DiseaseState> diseases = new HashMap<>();
    public final List<String> requiredSlots = new ArrayList<>();
    
    public float getDiseaseProgress(String name) {
        return Optional.ofNullable(diseases.get(name))
                .map(state -> state.current)
                .orElse(0f);
    }
}
