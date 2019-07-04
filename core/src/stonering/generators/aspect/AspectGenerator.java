package stonering.generators.aspect;

import stonering.entity.Aspect;
import stonering.entity.Entity;
import stonering.entity.item.aspects.FallingAspect;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Savva Kodeikin
 */
public class AspectGenerator {

    private static final Map<String, Class<? extends Aspect>> aspectMapping;

    static {
        aspectMapping = new HashMap<>();
        aspectMapping.put("falling", FallingAspect.class);
    }

    public static Optional<Aspect> createAspect(String name, Entity entity) {
        Class aspectClass = aspectMapping.get(name);
        Optional<Aspect> maybeAspect = Optional.empty();

        if (aspectClass != null) {

            try {
                Class[] types = { Entity.class };
                Constructor<Aspect> constructor = aspectClass.getConstructor(types);
                Object[] params = {entity};
                maybeAspect = Optional.of(constructor.newInstance(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return maybeAspect;
    }
}
