package stonering.enums.materials;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Bean to be red from json and processed into {@link Material}.
 *
 * @author Alexander on 04.12.2019.
 */
public class RawMaterial {
    public int id;
    public String name;
    public ArrayList<String> tags;
    public float density;
    public HashMap<String, ArrayList<Object>> reactions; // other aspects
    public int value;
    public byte atlasY;
    public String color;
}
