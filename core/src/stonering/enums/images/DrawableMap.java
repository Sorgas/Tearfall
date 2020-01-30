package stonering.enums.images;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import stonering.stage.renderer.AtlasesEnum;
import stonering.util.global.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.badlogic.gdx.utils.JsonWriter.OutputType.json;
import static stonering.util.global.FileLoader.*;

/**
 * Implements lazy-loading for {@link Drawable}s.
 * There are three types of sprite resources in the game:
 * 1. REGION - rectangular part of texture with custom size,
 * 2. ICON - rectangular part of texture with fixed size,
 * 3. TEXTURE - whole texture.
 * /resources/drawable/[type]/ is source for {@link DrawableDescriptor}.
 * Loads {@link DrawableDescriptor}s from regions.json on startup.
 * <p>
 * Creates {@link TextureRegionDrawable}s from descriptors on demand.
 * Saves drawables in a maps by descriptor's name for faster access.
 * Works with non-tiled atlases.
 */
public enum DrawableMap {
    REGION(desc -> new TextureRegion(getTexture(desc.texture), desc.bounds[0], desc.bounds[1], desc.bounds[2], desc.bounds[3]),
            DRAWABLE_DESCRIPTORS_PATH + "regions",
            null),
    ICON(desc -> AtlasesEnum.icons.getBlockTile(desc.bounds[0], desc.bounds[1]),
            DRAWABLE_DESCRIPTORS_PATH + "icons",
            "sprites/icons.png"),
    TEXTURE(desc -> new TextureRegion(getTexture(desc.texture)),
            DRAWABLE_DESCRIPTORS_PATH + "textures",
            null); //TODO

    private static final Map<String, Texture> textures = new HashMap<>(); // all textures in game
    private static final DrawableDescriptor DEFAULT_DESCRIPTOR = new DrawableDescriptor("default", "default.png", new int[]{0, 0, 64, 64});

    private Map<String, DrawableDescriptor> descriptors; // all descriptors
    private Map<String, Drawable> drawables; // loaded drawables
    private Function<DrawableDescriptor, TextureRegion> regionProducer; // contains logic for using data from descriptor

    DrawableMap(Function<DrawableDescriptor, TextureRegion> producer, String path, String textureOverride) {
        regionProducer = producer;
        descriptors = new HashMap<>();
        drawables = new HashMap<>();
        loadDescriptors(path);
    }

    private void loadDescriptors(String path) {
        Logger.LOADING.logDebug("loading drawables");
        Stream.of(new FileHandle(path).list())
                .map(fileHandle -> (ArrayList<DrawableDescriptor>) new Json(json).fromJson(ArrayList.class, DrawableDescriptor.class, fileHandle))
                .flatMap(Collection::stream).forEach(descriptor -> descriptors.put(descriptor.name, descriptor));
    }

    public Drawable getDrawable(String key) {
        DrawableDescriptor descriptor = descriptors.getOrDefault(key, DEFAULT_DESCRIPTOR); // resolve descriptor
        textures.putIfAbsent(descriptor.texture, new Texture("sprites/" + descriptor.texture)); // load new texture if needed
        drawables.putIfAbsent(key, new TextureRegionDrawable(regionProducer.apply(descriptor))); // lazy load drawable
        return drawables.get(key);
    }

    private static Texture getTexture(String key) {
        return textures.get(key);
    }
}
