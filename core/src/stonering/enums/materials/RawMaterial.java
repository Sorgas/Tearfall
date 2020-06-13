package stonering.enums.materials;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import stonering.entity.material.Material;

/**
 * Bean to be red from json and processed into {@link Material}.
 *
 * @author Alexander on 04.12.2019.
 */
public class RawMaterial {
    public String name;
    public ArrayList<String> tags;
    public float density;
    public HashMap<String, ArrayList<String>> reactions; // other aspects
    public int value;
    public byte atlasY;
    public String color;
    public float workAmountModifier = 1;
    public List<String> aspects; // "aspect(params)"
}
