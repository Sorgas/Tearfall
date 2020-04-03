package stonering.stage.building;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import stonering.entity.item.Item;
import stonering.enums.images.DrawableMap;
import stonering.enums.items.recipe.Ingredient;
import stonering.enums.materials.MaterialMap;
import stonering.game.model.system.item.ItemsStream;
import stonering.util.global.StaticSkin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.scenes.scene2d.ui.Tree.*;

/**
 * Part of {@link BuildingMaterialTab}.
 * Collects all items for given ingredient, groups them by type and material, and shows in a tree structure.
 * TODO when dragging over checkboxes or material type nodes, they should change their state.
 *
 * @author Alexander on 25.03.2020
 */
public class MaterialItemsSelectSection extends Container<Table> {
    private final int WIDTH = 200;
    private Table table;
    private Tree tree;

    public MaterialItemsSelectSection(Ingredient ingredient, String title) {
        setActor(table = new Table());
        table.defaults().growX().left();
        table.add(new Label(title, StaticSkin.getSkin())).colspan(2).row();
        table.add(createEnablingButton("allow all", true));
        table.add(createEnablingButton("clear all", false)).row();
        tree = createTree(ingredient);
        if(tree.getChildren().isEmpty()) {
            table.add(new Label("No items found", StaticSkin.getSkin()));
        } else {
            table.add(new ScrollPane(tree)).colspan(2);
        }
        table.setDebug(true, true);
        table.top();
        fill().top();
        table.setBackground(DrawableMap.REGION.getDrawable("default"));
    }

    /**
     * Finds items for ingredient, groups them by type and material and put into tree.
     * If no items of certain type were found, type node is not added.
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
                map.forEach((materialId, list) -> {
                    String material = MaterialMap.instance().getMaterial(materialId).name;
                    MaterialTypeNode materialRow = new MaterialTypeNode(type, material, ingredient.quantity, list.size()); // create widget for material
                    typeRow.add(materialRow); // add node into tree
                    ((Container) materialRow.getActor()).width(WIDTH - tree.getIndentSpacing()); // tree cannot size its nodes
                    materialRow.table.layout();
                });
            }
        }
        tree.setDebug(true, true);
        return tree;
    }

    private TextButton createEnablingButton(String text, boolean enable) {
        TextButton button = new TextButton(text, StaticSkin.getSkin());
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                setAllEnabled(enable);
            }
        });
        return button;
    }

    private void setAllEnabled(boolean enable) {
        for (Node node : tree.getRootNodes()) {
            ((TypeNode) node).toggleAll(enable);
        }
    }
}
