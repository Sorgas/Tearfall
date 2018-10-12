package stonering.entity.local.items.aspects;

import stonering.entity.local.Aspect;

import java.util.ArrayList;

/**
 * Generic aspect for storing some property`s value
 *
 * @author Alexander Kuzyakov on 28.01.2018.
 */
public class TagAspect extends Aspect{
    private ArrayList<String> tags;

    public TagAspect() {
        super("tag", null);
        tags = new ArrayList<>();
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void addTag(String tag) {
        tags.add(tag);
    } 

    public boolean hasTag(String tag) {
        return tags.contains(tag);
    }
}
