package stonering.enums.plants;

import stonering.global.utils.Position;

/**
 * @author Alexander Kuzyakov on 30.10.2017.
 */
public class TreeType {
    private String specimen;
    private String materialName;
    private int crownRadius;
    private int height;
    private int rootDepth;
    private int rootRadius;
    private int treeRadius;

    public Position getStompPosition() {
        return new Position(crownRadius, crownRadius, rootDepth);
    }

    public int getTreeRadius() {
        return treeRadius;
    }

    public String getSpecimen() {
        return specimen;
    }

    public void setSpecimen(String specimen) {
        this.specimen = specimen;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public int getCrownRadius() {
        return crownRadius;
    }

    public void setCrownRadius(int crownRadius) {
        this.crownRadius = crownRadius;
        treeRadius = crownRadius > rootRadius ? crownRadius : rootRadius;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getRootDepth() {
        return rootDepth;
    }

    public void setRootDepth(int rootDepth) {
        this.rootDepth = rootDepth;
    }

    public int getRootRadius() {
        return rootRadius;
    }

    public void setRootRadius(int rootRadius) {
        this.rootRadius = rootRadius;
        treeRadius = crownRadius > rootRadius ? crownRadius : rootRadius;
    }
}