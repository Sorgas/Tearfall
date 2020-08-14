package stonering.entity.unit.aspects;

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
    public final int base;
    public Map<String, MoodEffect> effects;
    public int total;
    public boolean changed;
    public Task task;

    public MoodAspect(int base) {
        this.base = base;
    }

    /**
     * Adds new, or replaces old effect.
     */
    public void addEffect(MoodEffect effect) {
        MoodEffect oldEffect = effects.put(effect.id, effect);
        if(oldEffect != null && oldEffect.value != effect.value) {
            total = base + effects.values().stream()
                    .map(eff -> eff.value)
                    .reduce(Integer::sum)
                    .orElse(0);
            // total
        }
    }
}
