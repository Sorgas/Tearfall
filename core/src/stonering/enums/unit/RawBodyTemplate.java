package stonering.enums.unit;

import java.util.List;

/**
 * Raw object for {@link BodyTemplate}. Loaded from json and then processed into {@link BodyTemplate}.
 *
 * @author Alexander_Kuzyakov on 02.07.2019.
 */
public class RawBodyTemplate {
    public String name;
    public List<String> defaultLayers;
    public List<String> needs;
    public List<RawBodyPart> body;

    public static class RawBodyPart implements Cloneable {
        public String name;
        public String type;
        public String root;
        public String[] internal;
        public String[] tags;
        public String[] layers;
        public int size;
        public boolean mirrored = false;

        public RawBodyPart(RawBodyPart rawBodyPart) {
            name = rawBodyPart.name;
            type = rawBodyPart.type;
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
}
