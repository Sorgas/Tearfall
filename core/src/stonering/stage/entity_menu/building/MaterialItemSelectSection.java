package stonering.stage.entity_menu.building;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.buildings.blueprint.MaterialSelectionConfig;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.OnMapItemsStream;
import stonering.util.lang.StaticSkin;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.scenes.scene2d.ui.Tree.*;

/**
 * Part of {@link BuildingMaterialTab}.
 * Collects all items for given ingredient, groups them by type and material, and shows in a tree structure.
 * Produces {@link ItemSelector} of current section state.
 * Saves player's selection in {@link MaterialSelectionConfig} and restores it when same blueprint is selected again.
 * Shows nodes for selection, even if there are insufficient items available.
 * 
 * TODO when dragging over checkboxes or material type nodes, they should change their state.
 * TODO add option to see all possible materials for item type (with checkbox).
 * TODO sort material nodes by amount on stocks.
 *
 * @author Alexander on 25.03.2020
 */
public class MaterialItemSelectSection extends ItemSelectSection {
    private Tree<ItemSelectionNode, String> tree;
    private Ingredient ingredient;
    public Map<String, List<MaterialTypeNode>> nodeMap = new HashMap<>();
    public final MaterialSelectionConfig config;

    public MaterialItemSelectSection(Ingredient ingredient, String title, MaterialSelectionConfig config) {
        super(title);
        this.ingredient = ingredient;
        this.config = config;
        tree = new Tree(StaticSkin.getSkin());
        createTree();
        if (tree.getChildren().isEmpty()) {
            System.out.println("tree not added");
            table.add(new Label("No items found", StaticSkin.getSkin()));
        } else {
            System.out.println("tree added");
            table.add(new ScrollPane(tree)).colspan(2);
            tree.getSelection().setDisabled(true);
        }
    }

    /**
     * Finds items for ingredient, groups them by type and material and put into tree.
     * If no items of certain type were found, type node is not added.
     * Also fills map for collecting selected materials.
     */
    private void createTree() {
        for (String type : ingredient.itemTypes) {
            // collect items
            Map<Integer, List<Item>> map = new HashMap<>(); // items grouped by material
            new OnMapItemsStream().filterByType(type).stream // collect items from map
                    .forEach(item -> map.computeIfAbsent(item.material, material -> new ArrayList<>()).add(item)); // group items by material
            if (map.isEmpty()) continue; // no rows for no items

            // create nodes
            TypeNode typeRow = addTypeNode(type, map);
            map.forEach((material, list) -> {
                MaterialTypeNode materialRow = new MaterialTypeNode(type, material, ingredient.quantity, list.size(), config); // create widget for material
                typeRow.addMaterialNode(materialRow); // add node into tree
                nodeMap.putIfAbsent(type, new ArrayList<>());
                nodeMap.get(type).add(materialRow);
                materialRow.width(WIDTH - tree.getIndentSpacing()); // tree cannot size its nodes
            });
            initNodes();
        }
        tree.setDebug(true, true);
    }

    private TypeNode addTypeNode(String type, Map<Integer, List<Item>> map) {
        TypeNode node = new TypeNode(type, map.values().stream().map(List::size).reduce(Integer::sum).orElse(0), config); // create widget for item type
        tree.add(node); // add node into tree
        node.width(WIDTH); // tree cannot size its nodes
        return node;
    }

    private void initNodes() {
        for (Node rootNode : tree.getRootNodes()) {
            TypeNode typeRow = (TypeNode) rootNode;
            if(config.types.contains(typeRow.type)) {
                typeRow.set(true); // overrides material settings
            } else if (config.map.containsKey(typeRow.type)){
                typeRow.nodes.stream().filter(node -> config.map.get(typeRow.type).contains(node.material)).forEach(node -> node.set(true));
            }
        }
    }

    /**
     * Produces item selector from config.
     */
    @Override
    public ItemSelector getItemSelector() {
        Map<String, Set<Integer>> map = new HashMap<>();
        nodeMap.forEach((key, value) -> {
            Set<Integer> materials = value.stream().filter(MaterialTypeNode::checked).map(node -> node.material).collect(Collectors.toSet());
            if (!materials.isEmpty()) map.put(key, materials);
        });
        return new ConfiguredItemSelector(map);
    }

    @Override
    protected void setAllEnabled(boolean enable) {
        for (Node node : tree.getRootNodes()) {
            ((TypeNode) node).set(enable);
        }
    }

    @Override
    public boolean isAtLeastOneSelected() {
        return nodeMap.values().stream().flatMap(Collection::stream).anyMatch(ItemSelectionNode::checked);
    }
}
