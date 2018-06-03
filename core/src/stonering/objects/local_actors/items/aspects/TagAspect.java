package stonering.objects.local_actors.items.aspects;

import stonering.objects.local_actors.Aspect;

import java.util.ArrayList;

/**
 * Created by Alexander on 28.01.2018.
 *
 * generic aspect for storing some property`s value
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
