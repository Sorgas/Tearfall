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

    public class RawBodyPart {
        public String name;
        public String type;
        public String root;
        public String[] internal;
        public String[] tags;
        public int size;
        public boolean mirrored = false;
    }
}
