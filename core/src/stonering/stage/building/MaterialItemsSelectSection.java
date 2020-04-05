package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import stonering.entity.item.Item;
import stonering.entity.item.selectors.ConfiguredItemSelector;
import stonering.entity.item.selectors.ItemSelector;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.recipe.Ingredient;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.global.StaticSkin;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.badlogic.gdx.scenes.scene2d.ui.Tree.*;

/**
 * Part of {@link BuildingMaterialTab}.
 * Collects all items for given ingredient, groups them by type and material, and shows in a tree structure.
 * Produces {@link ItemSelector} of current section state.
 *
 * TODO when dragging over checkboxes or material type nodes, they should change their state.
 *
 * @author Alexander on 25.03.2020
 */
public class MaterialItemsSelectSection extends ItemsSelectSection {
    private Tree tree;
    public Map<String, List<MaterialTypeNode>> nodeMap = new HashMap<>();

    public MaterialItemsSelectSection(Ingredient ingredient, String title) {
        super(title);
        tree = createTree(ingredient);
        if(tree.getChildren().isEmpty()) {
            table.add(new Label("No items found", StaticSkin.getSkin()));
        } else {
            table.add(new ScrollPane(tree)).colspan(2);
        }
    }

    /**
     * Finds items for ingredient, groups them by type and material and put into tree.
     * If no items of certain type were found, type node is not added.
     * Also fills map for collecting selected materials.
     */
    private Tree createTree(Ingredient ingredient) {
        Tree tree = new Tree(StaticSkin.getSkin());
        for (String type : ingredient.itemTypes) {
            Map<Integer, List<Item>> map = new HashMap<>();
            new ItemsStream().filterByType(type).stream.forEach(item -> { // group items by material
                map.putIfAbsent(item.material, new ArrayList<>());
                map.get(item.material).add(item);
            });
            if (!map.isEmpty()) { // create regular tree
                TypeNode typeRow = new TypeNode(type, map.values().stream().map(List::size).reduce(Integer::sum).orElse(0)); // create widget for item type
                tree.add(typeRow); // add node into tree
                ((Container) typeRow.getActor()).width(WIDTH); // tree cannot size its nodes
                map.forEach((material, list) -> {
                    MaterialTypeNode materialRow = new MaterialTypeNode(type, material, ingredient.quantity, list.size()); // create widget for material
                    typeRow.add(materialRow); // add node into tree
                    nodeMap.putIfAbsent(type, new ArrayList<>());
                    nodeMap.get(type).add(materialRow);
                    ((Container) materialRow.getActor()).width(WIDTH - tree.getIndentSpacing()); // tree cannot size its nodes
                });
            }
        }
        tree.setDebug(true, true);
        return tree;
    }

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
            ((TypeNode) node).toggleAll(enable);
        }
    }

    @Override
    public boolean isAtLeastOneSelected() {
        return nodeMap.values().stream().flatMap(Collection::stream).anyMatch(ItemSelectionNode::checked);
    }
}
