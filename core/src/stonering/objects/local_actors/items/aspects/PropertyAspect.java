package stonering.objects.local_actors.items.aspects;

import stonering.objects.local_actors.Aspect;
import stonering.objects.local_actors.AspectHolder;

import java.util.ArrayList;

/**
 * Created by Alexander on 28.01.2018.
 */
public class PropertyAspect extends Aspect{
    private ArrayList<String> properties;

    public PropertyAspect(String name, AspectHolder aspectHolder) {
        super(name, aspectHolder);
    }

    public ArrayList<String> getProperties() {
        return properties;
    }

    public void addProperty(String reaction) {
        properties.add(reaction);
    } 

    public boolean hasProperty(String property) {
        return properties.contains(property);
    }
}
