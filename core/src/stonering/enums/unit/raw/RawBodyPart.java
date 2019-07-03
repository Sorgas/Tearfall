package stonering.enums.unit.raw;

import java.util.List;

/**
 * Raw body part with strings red from body_templates.json.
 *
 * @author Alexander_Kuzyakov on 03.07.2019.
 */
public class RawBodyPart implements Cloneable {
    public String name;
    public String type;
    public String root;
    public List<String> internal;
    public List<String> tags;
    public List<String> layers;
    public int size;
    public boolean mirrored = false;

    // for json reader
    public RawBodyPart() {
    }

    public RawBodyPart(RawBodyPart rawBodyPart) {
        type = rawBodyPart.type;
        name = rawBodyPart.name;
        if (name == null) name = type;
        root = rawBodyPart.root;
        internal = rawBodyPart.internal;
        tags = rawBodyPart.tags;
        layers = rawBodyPart.layers;
        size = rawBodyPart.size;
        mirrored = rawBodyPart.mirrored;
    }

    @Override
    protected Object clone() {
        return new RawBodyPart(this);
    }
}

