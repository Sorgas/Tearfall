package stonering.entity.unit.aspects;

import java.util.HashMap;
import java.util.Map;

import stonering.entity.Aspect;
import stonering.entity.job.Task;

/**
 * Stores mood of a creature. Different effects may change it.
 * Stores task created for recreation.
 * 
 * @author Alexander on 14.08.2020.
 */
public class MoodAspect extends Aspect {
    public Map<String, MoodEffect> effects = new HashMap<>();
    public int total;
    public boolean changed;
    public Task task;

    /**
     * Adds new, or replaces old effect.
     */
    public void addEffect(MoodEffect effect) {
        if(effect == null) return;
        effects.put(effect.id, effect);
        updateTotal();
    }
    
    public void updateTotal() {
        int old = total;
        total = effects.values().stream()
                .map(eff -> eff.value)
                .reduce(Integer::sum)
                .orElse(0);
        changed = total != old;
    }
}
