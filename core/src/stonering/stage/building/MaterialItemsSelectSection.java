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
 *
 * @author Alexander on 25.03.2020
 */
public class MaterialItemsSelectSection extends Container<Table> {
    private final Tree tree;

    public MaterialItemsSelectSection(Ingredient ingredient, String title) {
        Table table = new Table();
        table.defaults().growX().left();
        table.add(new Label(title, StaticSkin.getSkin())).colspan(2).row();
        table.add(createButton("allow all", true));
        table.add(createButton("clear all", false)).row();
        table.add(new ScrollPane(tree = createTree(ingredient))).colspan(2).growY();
        width(200);
        setActor(table);
        this.fill();
        table.setDebug(true, true);
        setBackground(DrawableMap.REGION.getDrawable("default"));
    }

    private Tree createTree(Ingredient ingredient) {
        Tree tree = new Tree(StaticSkin.getSkin());
        ingredient.itemTypes.forEach(type -> {
            TypeNode typeRow = new TypeNode(type, 0); // create widget for item type
            tree.add(typeRow); // add node into tree
            Map<Integer, List<Item>> map = new HashMap<>();
            new ItemsStream().filterByType(type).stream.forEach(item -> { // group items by material
                map.putIfAbsent(item.material, new ArrayList<>());
                map.get(item.material).add(item);
            });
            map.keySet().forEach(materialId -> {
                String material = MaterialMap.instance().getMaterial(materialId).name;
                MaterialTypeNode materialRow = new MaterialTypeNode(type, material); // create widget for material
                typeRow.add(materialRow); // add node into tree
            });
        });
        return tree;
    }

    private TextButton createButton(String text, boolean enable) {
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
