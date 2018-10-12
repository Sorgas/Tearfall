package stonering.generators.aspect;

import stonering.entity.local.Aspect;
import stonering.entity.local.AspectHolder;
import stonering.entity.local.items.aspects.FallingAspect;

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

    public static Optional<Aspect> createAspect(String name, AspectHolder aspectHolder) {
        Class aspectClass = aspectMapping.get(name);
        Optional<Aspect> maybeAspect = Optional.empty();

        if (aspectClass != null) {

            try {
                Class[] types = { AspectHolder.class };
                Constructor<Aspect> constructor = aspectClass.getConstructor(types);
                Object[] params = { aspectHolder };
                maybeAspect = Optional.of(constructor.newInstance(params));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return maybeAspect;
    }
}
