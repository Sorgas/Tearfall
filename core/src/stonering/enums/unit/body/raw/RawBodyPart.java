package stonering.enums.unit.body.raw;

import java.util.ArrayList;
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
    public List<String> external;
    public List<String> tags;
    public List<String> layers;
    public int size;
    public boolean mirrored = false;

    // for json reader
    public RawBodyPart() {
        internal = new ArrayList<>();
        external = new ArrayList<>();
        tags = new ArrayList<>();
        layers = new ArrayList<>();
    }

    public RawBodyPart(RawBodyPart rawBodyPart) {
        type = rawBodyPart.type;
        name = rawBodyPart.name;
        root = rawBodyPart.root;
        internal = rawBodyPart.internal;
        external = rawBodyPart.external;
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

